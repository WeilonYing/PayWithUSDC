# Gasless Wallet

Gasless is a non-custodial wallet for MCD Dai native meta-transactions.
It leverages Dai's new approve by signature feature (aka permit function).

Read more about the making of Gasless: https://medium.com/mosendo/gasless-by-mosendo-3030f5e99099

This repo contains contracts and the frontend of the Gasless wallet

## Use the Gasless relayer in your own project

See https://github.com/mosendo/gasless.js

## Truffle Contracts

You need the Truffle Suite installed on your machine.
```sh
sudo npm i -g truffle
```

### Development

1. Run Truffle development environment
```sh
truffle develop
```
2. Migrate contracts
```sh
migrate
```
### Mainnet Deployment
1. Set private key as environment variable
```sh
export PRIVKEY="YOURPRIVATEKEY"
```
2. Migrate to mainnet
```sh
truffle migrate --network="live"
```

## Vue App

The vue app is located at the [/app](/app) directory
```sh
cd app/
```

### Development
```sh
npm run serve
```
### Bundle
1. Set the Infura Access Key environment variable:
```sh
export INFURA_ACCESS_KEY="YOURACCESSKEYHERE"
```
2. Build!
```sh
npm run build
```
The bundle will be placed at [/app/dist](/app/dist)
