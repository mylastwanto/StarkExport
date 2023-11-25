package com.starkexport.web;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.SpreadSheet;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExportController {
    private static final Logger log = LoggerFactory.getLogger(ExportController.class);

    @Value("${provider.apikey}")
    private String providerApiKey;

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
            case "txt":
                httpServletResponse.setHeader("Content-Type", "text/plain");
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt");
                break;
            case "json":
                httpServletResponse.setHeader("Content-Type", "application/json");
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".json");
                break;
            case "ods":
                httpServletResponse.setHeader("Content-Type", "application/vnd.oasis.opendocument.spreadsheet");
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".ods");
                break;
            case "html":
                httpServletResponse.setHeader("Content-Type", "text/html");
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".html");
                break;

        }

        //todo singleton
        Provider provider = new JsonRpcProvider(AppConstant.STARKNET_MAINNET_RPC, StarknetChainId.MAINNET);

        int lastBlockNumber = provider.getBlockNumber().send();
        
        StarkscanNetwork starkscanNetwork = StarkscanServiceGenerator.createService(StarkscanNetwork.class, AppConstant.STARKSCAN_MAINNET_API_URL);

        Call<Transaction> responseCall  = starkscanNetwork.retrieveTransaction(providerApiKey, 0, lastBlockNumber, contractAddress, "desc", 100);

        try {
            retrofit2.Response<Transaction> response = responseCall.execute();
            Transaction transaction = response.body();

            if (output.equalsIgnoreCase("xlsx")) {
                writeTransactionExcel(httpServletResponse, transaction);
            } else if (output.equalsIgnoreCase("txt")) {
                writeTransactionTxt(httpServletResponse, transaction);
            } else if (output.equalsIgnoreCase("ods")) {
                writeTransactionOds(httpServletResponse, transaction);
            } else if (output.equalsIgnoreCase("json")) {
                writeTransactionJson(httpServletResponse, transaction);
            } else if (output.equalsIgnoreCase("html")){
                writeTransactionHtml(httpServletResponse, transaction);
            } else {
                writeTransactionCsv(httpServletResponse, transaction);
            }

        } catch (IOException ioe){
            log.error(ioe.getMessage());
        } catch (Exception e){
            log.error(e.getMessage());
        }

    }

    private void writeTransactionHtml(HttpServletResponse httpServletResponse, Transaction transaction) throws Exception {
        OutputStreamWriter writer = new OutputStreamWriter(httpServletResponse.getOutputStream());

        StringBuilder content = new StringBuilder();

        content.append("<!DOCTYPE html><html><head><title>Starknet Transactions</title></head><body><table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:100%\"><tbody><tr><th>Nonce</th><th>DateTime UTC</th><th>From</th><th>TrxType</th><th>Method</th><th>TrxFee</th><th>BlockNo</th><th>Status</th><th>Transaction Hash</th></tr>");

        BigDecimal totalFee = new BigDecimal(0);

        for(Data data : transaction.getData()){

            content.append("<tr>");
            content.append("<td align='right'>").append(data.getNonce() == null ? 0 : Integer.parseInt(data.getNonce())).append("</td>");
            content.append("<td>").append(AppUtil.getUTCDate(data.getTimestamp())).append("</td>");
            content.append("<td>").append(StringUtils.defaultString(data.getSender_address())).append("</td>");
            content.append("<td>").append(data.getTransaction_type()).append("</td>");

            List<String> methodList = new ArrayList<>();

            for(AccountCall accountCall : data.getAccount_calls()){
                methodList.add(accountCall.getSelector_name());
            }

            content.append("<td>").append(String.join(",", methodList)).append("</td>");
            content.append("<td align='right'>").append(String.format("%.8f", Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER))).append("</td>");

            totalFee = totalFee.add(new BigDecimal(String.valueOf(Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER))));

            content.append("<td>").append(data.getBlock_number()).append("</td>");
            content.append("<td>").append(data.getTransaction_status()).append("</td>");
            content.append("<td><a href='https://starkscan.co/tx/").append(data.getTransaction_hash()).append("'>").append(data.getTransaction_hash()).append("</a></td>");
            content.append("</tr>");
        }

        content.append("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td align=\"right\"><strong>").append(Double.valueOf(String.format("%.8f", totalFee))).append("</strong></td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");

        content.append("</tbody></table></body></html>");
        writer.write(content.toString());
        writer.flush();

    }

    private void writeTransactionJson(HttpServletResponse httpServletResponse, Transaction transaction) throws Exception {
        OutputStreamWriter writer = new OutputStreamWriter(httpServletResponse.getOutputStream());
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonArray jsonArray = new JsonArray();
        for(Data data : transaction.getData()){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("nonce",data.getNonce() == null ? 0 : Integer.parseInt(data.getNonce()));
            jsonObject.addProperty("dateTimeUTC", AppUtil.getUTCDate(data.getTimestamp()));
            jsonObject.addProperty("from", StringUtils.defaultString(data.getSender_address()));
            jsonObject.addProperty("trxType", data.getTransaction_type());

            List<String> methodList = new ArrayList<>();

            for(AccountCall accountCall : data.getAccount_calls()){
                methodList.add(accountCall.getSelector_name());
            }

            jsonObject.addProperty("method", String.join(",", methodList));
            jsonObject.addProperty("trxFee", String.format("%.8f", Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER)));
            jsonObject.addProperty("blockNo", data.getBlock_number());
            jsonObject.addProperty("status", data.getTransaction_status());
            jsonObject.addProperty("transactionHash", data.getTransaction_hash());

            jsonArray.add(jsonObject);
        }

        writer.write(gson.toJson(jsonArray));
        writer.flush();

    }

    private void writeTransactionOds(HttpServletResponse httpServletResponse, Transaction transaction) throws Exception {

        int totalRow = transaction.getData().size() + 2;
        com.github.miachm.sods.Sheet sheet = new com.github.miachm.sods.Sheet("Transactions", totalRow, 9);

        SpreadSheet spreadSheet = new SpreadSheet();

        //set header
        sheet.getRange(0,0).setValue("Nonce");
        sheet.getRange(0,1).setValue("DateTime UTC");
        sheet.getRange(0,2).setValue("From");
        sheet.getRange(0,3).setValue("TrxType");
        sheet.getRange(0,4).setValue("Method");
        sheet.getRange(0,5).setValue("TrxFee");
        sheet.getRange(0,6).setValue("BlockNo");
        sheet.getRange(0,7).setValue("Status");
        sheet.getRange(0,8).setValue("TransactionHash");

        BigDecimal totalFee = new BigDecimal(0);

        int row = 1;
        for(Data data : transaction.getData()){
            sheet.getRange(row, 0).setValue(data.getNonce() == null ? 0 : Integer.parseInt(data.getNonce()));
            sheet.getRange(row, 1).setValue(AppUtil.getUTCDate(data.getTimestamp()));
            sheet.getRange(row, 2).setValue(StringUtils.defaultString(data.getSender_address()));
            sheet.getRange(row, 3).setValue(data.getTransaction_type());

            List<String> methodList = new ArrayList<>();

            for(AccountCall accountCall : data.getAccount_calls()){
                methodList.add(accountCall.getSelector_name());
            }

            sheet.getRange(row, 4).setValue(String.join(",", methodList));
            sheet.getRange(row, 5).setValue(Double.valueOf(String.format("%.8f", Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER))));

            totalFee = totalFee.add(new BigDecimal(String.valueOf(Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER))));


            sheet.getRange(row, 6).setValue(data.getBlock_number());
            sheet.getRange(row, 7).setValue(data.getTransaction_status());
            sheet.getRange(row, 8).setValue(data.getTransaction_hash());
            row++;
        }

        //add total fee
        sheet.getRange(row++,5).setValue(Double.valueOf(String.format("%.8f", totalFee)));;

        spreadSheet.appendSheet(sheet);
        spreadSheet.save(httpServletResponse.getOutputStream());

    }

    private void writeTransactionExcel(HttpServletResponse httpServletResponse, Transaction transaction) throws IOException {
        Workbook  workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");
        sheet.autoSizeColumn(5);

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

        BigDecimal totalFee = new BigDecimal(0);

        for(Data data : transaction.getData()){
            Row bodyRow = sheet.createRow(rowCount++);
            bodyRow.createCell(0).setCellValue(data.getNonce() == null ? 0 : Integer.parseInt(data.getNonce()));
            bodyRow.createCell(1).setCellValue(AppUtil.getUTCDate(data.getTimestamp()));
            bodyRow.createCell(2).setCellValue(StringUtils.defaultString(data.getSender_address()));
            bodyRow.createCell(3).setCellValue(data.getTransaction_type());

            List<String> methodList = new ArrayList<>();

            for(AccountCall accountCall : data.getAccount_calls()){
                methodList.add(accountCall.getSelector_name());
            }

            bodyRow.createCell(4).setCellValue(String.join(",", methodList));
            bodyRow.createCell(5).setCellValue(Double.valueOf(String.format("%.8f", Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER))));

            totalFee = totalFee.add(new BigDecimal(String.valueOf(Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER))));

            bodyRow.createCell(6).setCellValue(data.getBlock_number());
            bodyRow.createCell(7).setCellValue(data.getTransaction_status());
            bodyRow.createCell(8).setCellValue(data.getTransaction_hash());
        }

        //add total fee
        Row endRow = sheet.createRow(rowCount++);
        endRow.createCell(5).setCellValue(Double.valueOf(String.format("%.8f", totalFee)));

        workbook.write(httpServletResponse.getOutputStream());

    }

    private void writeTransactionTxt(HttpServletResponse httpServletResponse, Transaction transaction) throws IOException {
        PrintWriter writer = httpServletResponse.getWriter();

        String header = StringUtils.rightPad("Nonce", 7);
        header = header + StringUtils.rightPad("DateTime", 22);
        header = header + StringUtils.rightPad("From", 70);
        header = header + StringUtils.rightPad("TrxType", 20);
        header = header + StringUtils.rightPad("Method", 50);
        header = header + StringUtils.rightPad("TrxFee", 15);
        header = header + StringUtils.rightPad("BlockNo", 10);
        header = header + StringUtils.rightPad("Status", 20);
        header = header + "TransactionHash";

        writer.println(header);
        for(Data data : transaction.getData()){

            String line = "";
            line = line + StringUtils.rightPad(StringUtils.defaultString(data.getNonce()), 7);
            line = line + StringUtils.rightPad(AppUtil.getUTCDate(data.getTimestamp()), 22);
            line = line + StringUtils.rightPad(StringUtils.defaultString(data.getSender_address()), 70);
            line = line + StringUtils.rightPad(data.getTransaction_type(), 20);

            List<String> methodList = new ArrayList<>();

            for(AccountCall accountCall : data.getAccount_calls()){
                methodList.add(accountCall.getSelector_name());
            }

            line = line + StringUtils.rightPad(String.join(",", methodList), 50);
            line = line + StringUtils.rightPad(String.format("%.8f", Convert.fromWei(data.getActual_fee(), Convert.Unit.ETHER)), 15);
            line = line + StringUtils.rightPad(String.valueOf(data.getBlock_number()), 10);
            line = line + StringUtils.rightPad(data.getTransaction_status(), 20);
            line = line + data.getTransaction_hash();

            writer.println(line);
        }

        writer.close();

    }

    private void writeTransactionCsv(HttpServletResponse httpServletResponse, Transaction transaction) throws IOException {

        ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] header = { "Nonce", "DateTime", "From", "TrxType", "Method", "TrxFee", "BlockNo", "Status", "TransactionHash" };

        csvWriter.writeHeader(header);

        for(Data data : transaction.getData()){
            TransactionExport transactionExport = new TransactionExport();
            transactionExport.setNonce(StringUtils.defaultString(data.getNonce()));
            transactionExport.setDateTime(AppUtil.getUTCDate(data.getTimestamp()));
            transactionExport.setFrom(StringUtils.defaultString(data.getSender_address()));
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
