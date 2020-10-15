// SPDX-License-Identifier: MIT
pragma solidity >0.6.10;

//import "@opengsn/gsn/contracts/BaseRelayRecipient.sol";
import "./IERC20.sol";
import "./opengsn/BaseRelayRecipient.sol";
import "./opengsn/interfaces/IKnowForwarderAddress.sol";

// This is just a simple example of a coin-like contract.
// It is not standards compatible and cannot be expected to talk to other
// coin/token contracts. If you want to create a standards-compliant
// token, see: https://github.com/ConsenSys/Tokens. Cheers!

contract SendMoney is BaseRelayRecipient, IKnowForwarderAddress {
	string public override versionRecipient = "0.0.1";

	constructor(address _forwarder) public {
		trustedForwarder = _forwarder;
	}

	/**
	 * @param tokenAddr contract address of ERC20 token
	 * @param receiver address of receiver
	 * @param amount amount token amount in smallest denomination value
	 */
	function sendCoin(address tokenAddr, address receiver, uint amount) public returns(bool sufficient) {
		IERC20 token = IERC20(tokenAddr);
		token.approve(_msgSender(), amount);
		token.transferFrom(_msgSender(), receiver, amount);
		return true;
	}

	function getTrustedForwarder() public view override returns(address) {
		return trustedForwarder;
	}
}
