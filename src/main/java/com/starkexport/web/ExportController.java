package com.starkexport.web;

import com.starkexport.common.AppConstant;
import com.swmansion.starknet.data.types.StarknetChainId;
import com.swmansion.starknet.provider.Provider;
import com.swmansion.starknet.provider.rpc.JsonRpcProvider;
import org.springframework.stereotype.Controller;

@Controller
public class ExportController {

    public static void main(String[] args) {
        Provider provider = new JsonRpcProvider(AppConstant.STARKNET_MAINNET_RPC, StarknetChainId.MAINNET);
        int blockNumber = provider.getBlockNumber().send();
        System.out.println("blockNumber = " + blockNumber);
    }
}
