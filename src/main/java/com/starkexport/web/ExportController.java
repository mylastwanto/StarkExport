package com.starkexport.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.starkexport.common.AppConstant;
import com.starkexport.common.AppUtil;
import com.starkexport.model.TransactionExport;
import com.starkexport.model.pojo.AccountCall;
import com.starkexport.model.pojo.Data;
import com.starkexport.model.pojo.Transaction;
import com.starkexport.network.StarkscanNetwork;
import com.starkexport.network.StarkscanServiceGenerator;
import com.swmansion.starknet.data.types.StarknetChainId;
import com.swmansion.starknet.provider.Provider;
import com.swmansion.starknet.provider.rpc.JsonRpcProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.web3j.utils.Convert;
import retrofit2.Call;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExportController {
    private static final Logger log = LoggerFactory.getLogger(ExportController.class);

    @RequestMapping(value="/account/transactions", method = RequestMethod.GET)
    public void retrieveTransaction(@RequestParam(value = "address", required = true) String contractAddress, @RequestParam(value = "output", required = false, defaultValue = "xlsx") String output,
                                                 HttpServletResponse httpServletResponse){

        String fileName = "transaction-" + contractAddress;

        switch (output){
            case "xlsx":
                httpServletResponse.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
                break;
            case "csv":
                httpServletResponse.setHeader("Content-Type", "text/csv");
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv");
                break;
        }

        //todo singleton
        Provider provider = new JsonRpcProvider(AppConstant.STARKNET_MAINNET_RPC, StarknetChainId.MAINNET);

        int lastBlockNumber = provider.getBlockNumber().send();
        
        StarkscanNetwork starkscanNetwork = StarkscanServiceGenerator.createService(StarkscanNetwork.class, AppConstant.STARKSCAN_MAINNET_API_URL);

        Call<Transaction> responseCall  = starkscanNetwork.retrieveTransaction("", 0, lastBlockNumber, contractAddress, "desc", 100);

        try {
            retrofit2.Response<Transaction> response = responseCall.execute();
            Transaction transaction = response.body();

            if (output.equalsIgnoreCase("xlsx")){
                writeTransactionExcel(httpServletResponse, transaction);
            } else {
                writeTransactionCsv(httpServletResponse, transaction);
            }

        } catch (IOException ioe){
            log.error(ioe.getMessage());
        }

    }

    private void writeTransactionExcel(HttpServletResponse httpServletResponse, Transaction transaction) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        int rowCount = 0;

        Row row = sheet.createRow(rowCount++);

        row.createCell(0).setCellValue("Nonce");
        row.createCell(1).setCellValue("DateTime UTC");
        row.createCell(2).setCellValue("From");
        row.createCell(3).setCellValue("TrxType");
        row.createCell(4).setCellValue("Method");
        row.createCell(5).setCellValue("TrxFee");
        row.createCell(6).setCellValue("BlockNo");
        row.createCell(7).setCellValue("Status");
        row.createCell(8).setCellValue("TransactionHash");

        for(Data data : transaction.getData()){
            Row bodyRow = sheet.createRow(rowCount++);
            bodyRow.createCell(0).setCellValue(data.getNonce());
            bodyRow.createCell(1).setCellValue(AppUtil.getUTCDate(data.getTimestamp()));
            bodyRow.createCell(2).setCellValue(data.getSender_address());
            bodyRow.createCell(3).setCellValue(data.getTransaction_type());

            List<String> methodList = new ArrayList<>();

            for(AccountCall accountCall : data.getAccount_calls()){
                methodList.add(accountCall.getSelector_name());
            }

            bodyRow.createCell(4).setCellValue(String.join(",", methodList));
            bodyRow.createCell(5).setCellValue(Double.valueOf(String.format("%.8f", Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER))));
            bodyRow.createCell(6).setCellValue(data.getBlock_number());
            bodyRow.createCell(7).setCellValue(data.getTransaction_status());
            bodyRow.createCell(8).setCellValue(data.getTransaction_hash());
        }

        workbook.write(httpServletResponse.getOutputStream());

    }

    private void writeTransactionCsv(HttpServletResponse httpServletResponse, Transaction transaction) throws IOException {

        ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] header = { "Nonce", "DateTime", "From", "TrxType", "Method", "TrxFee", "BlockNo", "Status", "TransactionHash" };

        csvWriter.writeHeader(header);

        for(Data data : transaction.getData()){
            TransactionExport transactionExport = new TransactionExport();
            transactionExport.setNonce(data.getNonce());
            transactionExport.setDateTime(AppUtil.getUTCDate(data.getTimestamp()));
            transactionExport.setFrom(data.getSender_address());
            transactionExport.setTrxType(data.getTransaction_type());

            List<String> methodList = new ArrayList<>();

            for(AccountCall accountCall : data.getAccount_calls()){
                methodList.add(accountCall.getSelector_name());
            }

            transactionExport.setMethod(String.join(",", methodList));
            transactionExport.setTrxFee(String.format("%.8f", Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER)));
            transactionExport.setBlockNo(String.valueOf(data.getBlock_number()));
            transactionExport.setStatus(data.getTransaction_status());
            transactionExport.setTransactionHash(data.getTransaction_hash());

            csvWriter.write(transactionExport, header);
        }
        csvWriter.close();
    }



}
