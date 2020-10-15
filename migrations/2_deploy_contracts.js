const SendMoneyDirect = artifacts.require("SendMoneyDirect");
const SendMoney = artifacts.require("SendMoney");

module.exports = function(deployer) {
  deployer.deploy(SendMoneyDirect);
  //deployer.deploy(SendMoney);
};
