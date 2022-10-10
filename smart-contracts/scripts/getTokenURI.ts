import dotenv from "dotenv"
dotenv.config()

import { ethers } from "hardhat";
const erc721Address: string = process.env.ERC721_ADDRESS as string;

import ERC721ABI from './erc721abi.json';


async function main() {
    const accounts = await ethers.getSigners();
    const user = accounts[0];
    const erc721 = new ethers.Contract(erc721Address, ERC721ABI, user);
    console.log(`Sending request for reading tokenURI`)
    const result = await erc721.tokenURI('3');
    console.log(`result  : ${result}`)
}
// We recommend this pattern to be able to use async/await everywhere
// and properly handle errors.
main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});