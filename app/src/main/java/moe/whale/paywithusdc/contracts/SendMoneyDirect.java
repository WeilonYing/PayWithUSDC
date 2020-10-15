package moe.whale.paywithusdc.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.7.0.
 */
@SuppressWarnings("rawtypes")
public class SendMoneyDirect extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610234806100206000396000f3fe608060405234801561001057600080fd5b5060043610610047577c010000000000000000000000000000000000000000000000000000000060003504630b40bd88811461004c575b600080fd5b61008f6004803603606081101561006257600080fd5b5073ffffffffffffffffffffffffffffffffffffffff8135811691602081013590911690604001356100a3565b604080519115158252519081900360200190f35b604080517f095ea7b3000000000000000000000000000000000000000000000000000000008152336004820152602481018390529051600091859173ffffffffffffffffffffffffffffffffffffffff83169163095ea7b391604480830192602092919082900301818887803b15801561011c57600080fd5b505af1158015610130573d6000803e3d6000fd5b505050506040513d602081101561014657600080fd5b5050604080517f23b872dd00000000000000000000000000000000000000000000000000000000815233600482015273ffffffffffffffffffffffffffffffffffffffff8681166024830152604482018690529151918316916323b872dd916064808201926020929091908290030181600087803b1580156101c757600080fd5b505af11580156101db573d6000803e3d6000fd5b505050506040513d60208110156101f157600080fd5b506001969550505050505056fea2646970667358221220e24a2e5a62c60a96a974aba0b232064b90013595be961e66ad0f846d1d0afb7f64736f6c63430007010033";

    public static final String FUNC_SENDCOIN = "sendCoin";

    @Deprecated
    protected SendMoneyDirect(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SendMoneyDirect(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SendMoneyDirect(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SendMoneyDirect(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> sendCoin(String tokenAddr, String receiver,
            BigInteger amount) {
        final Function function = new Function(
                FUNC_SENDCOIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, tokenAddr), 
                new org.web3j.abi.datatypes.Address(160, receiver), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SendMoneyDirect load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new SendMoneyDirect(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SendMoneyDirect load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SendMoneyDirect(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SendMoneyDirect load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new SendMoneyDirect(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SendMoneyDirect load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SendMoneyDirect(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SendMoneyDirect> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SendMoneyDirect.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SendMoneyDirect> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SendMoneyDirect.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SendMoneyDirect> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SendMoneyDirect.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SendMoneyDirect> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SendMoneyDirect.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
