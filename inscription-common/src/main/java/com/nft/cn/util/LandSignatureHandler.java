package com.nft.cn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class LandSignatureHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final String version = "1";
	private ECKeyPair ecKeyPair;

	private final String EIP712_DOMAIN = "";
	private final String MINT_PERMIT = "";
	private final String WITHDRAW_PERMIT = "";
	private final String PAY_PERMIT = "";
	private final String SWAP_PERMIT = "";
	private final String BRIDGE_PAY_PERMIT = "";

	private byte[] EIP712_DOMAIN_TYPEHASH = Hash.sha3(EIP712_DOMAIN.getBytes());
	private byte[] MINT_PERMIT_TYPEHASH = Hash.sha3(MINT_PERMIT.getBytes());
	private byte[] WITHDRAW_TYPEHASH = Hash.sha3(WITHDRAW_PERMIT.getBytes());
	private byte[] PAY_TYPEHASH = Hash.sha3(PAY_PERMIT.getBytes());
	private byte[] SWAP_TYPEHASH = Hash.sha3(SWAP_PERMIT.getBytes());
	private byte[] BRIDGE_PAY_TYPEHASH = Hash.sha3(BRIDGE_PAY_PERMIT.getBytes());

	private byte[] EIP712_DOMAIN_HASH;

	private String encodePacked(String bohash) {
		return Numeric.toHexString(Hash.sha3(Numeric.hexStringToByteArray("0x1901" + Numeric.toHexStringNoPrefix(EIP712_DOMAIN_HASH) + bohash)));
	}

	public LandSignatureHandler(String contract, Integer chainId, String dappName, String privKey) {
		List<Type> params = new ArrayList<Type>();
		EIP712_DOMAIN_HASH = Hash.sha3(Numeric.hexStringToByteArray(FunctionEncoder.encodeConstructor(params)));
	}

	public String userMint(String user,BigInteger price,Long order,Long nonce,Long deadlineSecond) {
		List<Type> result = new ArrayList<Type>();
		return "0x" + FunctionEncoder.encodeConstructor(result);
	}

	public String withdraw(String user,String receiver,Long order,BigInteger price,Long nonce,Long deadlineSecond) {
		List<Type> result = new ArrayList<Type>();
		return "0x" + FunctionEncoder.encodeConstructor(result);
	}

	public String pay(String user,String receiver,BigInteger fee,String feeReceiver,Long order,BigInteger price,Long nonce,Long deadlineSecond) {
		List<Type> result = new ArrayList<Type>();
		return "0x" + FunctionEncoder.encodeConstructor(result);
	}


	public String swap(String user,String token,Long inOrOut,BigInteger price,BigInteger fee,String feeReceiver,Long order,Long nonce,Long deadlineSecond) {
		List<Type> result = new ArrayList<Type>();
		return "0x" + FunctionEncoder.encodeConstructor(result);
	}


    public String bridgePay(String userAddress, String protocol, String tick, BigInteger amountIn, BigInteger amountOut, Long order, Long nonce, Long deadlineSecond) {
		List<Type> result = new ArrayList<Type>();
		return "0x" + FunctionEncoder.encodeConstructor(result);
    }
}
