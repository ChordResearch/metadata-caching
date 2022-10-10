import dotenv from "dotenv"
dotenv.config()

import { HardhatUserConfig } from "hardhat/config";
import "@nomicfoundation/hardhat-toolbox";

const key: string = process.env.PRIVATE_KEY as string;
const blastGoerliKey: string = process.env.BLAST_GOERLI_KEY as string;

const config: HardhatUserConfig = {
  defaultNetwork: "goerli",
  solidity: "0.8.9",
  networks: {
    hardhat: {
    },
    matic: {
      url: "https://rpc-mumbai.maticvigil.com",
      accounts: [key]
    },
    goerli: {
      url: `https://eth-goerli.blastapi.io/${blastGoerliKey}`,
      accounts: [key]
    }
  },
};

export default config;
