pragma solidity 0.5.11;

import "openzeppelin-solidity/contracts/math/SafeMath.sol";

interface USDC {
    function approve(address usr, uint wad) external returns (bool);
    function permit(address holder, address spender, uint256 nonce, uint256 expiry, bool allowed, uint8 v, bytes32 r, bytes32 s) external;
    function transfer(address dst, uint wad) external returns (bool);
    function transferFrom(address src, address dst, uint wad) external returns (bool);
}

interface UniswapExchange {
    function tokenToEthTransferInput(
        uint256 tokens_sold,
        uint256 min_eth,
        uint256 deadline,
        address recipient
    ) external returns (uint256  eth_bought);
}

contract Gasless {
    using SafeMath for uint;

    uint MAX_UINT256 = 2**256-1;
    USDC USDCContract;
    UniswapExchange UniswapExchangeContract;
    address relayer;
    bytes32 public DOMAIN_SEPARATOR;
    bytes32 public constant SEND_TYPEHASH = keccak256(
        "Send(address relayer,address to,uint256 value,uint256 fee,uint256 gasprice,uint256 nonce,uint256 deadline)"
    );
    bytes32 public constant SWAP_TYPEHASH = keccak256(
        "Swap(address relayer,uint256 usdc_sold,uint256 min_eth,uint256 fee,uint256 gasprice,uint256 nonce,uint256 deadline)"
    );

    event RelayerChange(address newRelayer);

    mapping (address => uint256) public nonces;

    modifier onlyRelayer () {
        require(msg.sender == relayer, "Only the relayer address is authorized to send this tx");
        _;
    }

    constructor(address initialRelayer, address USDCaddress, address UniswapExchangeAddress, uint chainId) public {
        USDCContract = USDC(USDCaddress);
        UniswapExchangeContract = UniswapExchange(UniswapExchangeAddress);
        relayer = initialRelayer;
        DOMAIN_SEPARATOR = keccak256(abi.encode(
            keccak256("EIP712Domain(string name,string version,uint256 chainId,address verifyingContract)"),
            keccak256("Gasless by Mosendo"),
            keccak256(bytes("1")),
            chainId,
            address(this)
        ));
        approveUniswap();
    }

    function changeRelayer(address newRelayer) public onlyRelayer {
        relayer = newRelayer;
        emit RelayerChange(newRelayer);
    }

    function send(address to, uint value, uint nonce, uint fee, uint deadline, uint8 v, bytes32 r, bytes32 s) public onlyRelayer {
        require(block.timestamp <= deadline, "Deadline expired");
        bytes32 digest = keccak256(abi.encodePacked(
                "\x19\x01",
                DOMAIN_SEPARATOR,
                keccak256(abi.encode(SEND_TYPEHASH,
                                     relayer,
                                     to,
                                     value,
                                     fee,
                                     tx.gasprice,
                                     nonce,
                                     deadline))
        ));
        address from = ecrecover(digest, v, r, s);
        require(nonce == nonces[from], "Invalid nonce");
        nonces[from]++;
        USDCContract.transferFrom(from, to, value.sub(fee));
        USDCContract.transferFrom(from, msg.sender, fee);
    }

    function usdcToEthSwap(
        uint usdc_sold,
        uint min_eth,
        uint nonce,
        uint fee,
        uint deadline,
        uint8 v,
        bytes32 r,
        bytes32 s
    ) public onlyRelayer {
        require(block.timestamp <= deadline, "Deadline expired");
        bytes32 digest = keccak256(abi.encodePacked(
                "\x19\x01",
                DOMAIN_SEPARATOR,
                keccak256(abi.encode(SWAP_TYPEHASH,
                                     relayer,
                                     usdc_sold,
                                     min_eth,
                                     fee,
                                     tx.gasprice,
                                     nonce,
                                     deadline))
        ));
        address from = ecrecover(digest, v, r, s);
        require(nonce == nonces[from], "Invalid nonce");
        nonces[from]++;
        USDCContract.transferFrom(from, msg.sender, fee);
        USDCContract.transferFrom(from, address(this), usdc_sold.sub(fee));
        UniswapExchangeContract.tokenToEthTransferInput(usdc_sold, min_eth, deadline, from);
    }

    function approveUniswap() public {
        USDCContract.approve(address(UniswapExchangeContract), MAX_UINT256);
    }

}