const HDWalletProvider = require("@truffle/hdwallet-provider");

module.exports = {
  // Uncommenting the defaults below 
  // provides for an easier quick-start with Ganache.
  // You can also follow this format for other networks;
  // see <http://truffleframework.com/docs/advanced/configuration>
  // for more details on how to specify configuration options!
  //
  //networks: {
  //  development: {
  //    host: "127.0.0.1",
  //    port: 7545,
  //    network_id: "*"
  //  },
  //  test: {
  //    host: "127.0.0.1",
  //    port: 7545,
  //    network_id: "*"
  //  }
  //}
  //
  compilers: {
    solc: {
      version: "^0.6.10",
    }
  },
  networks: {
    ropsten: {
      provider: function() {
        return new HDWalletProvider(
          "some mnemonic here",
          "https://ropsten.infura.io/v3/<api_key>")
      },
      network_id: 3
    }
  }
};
