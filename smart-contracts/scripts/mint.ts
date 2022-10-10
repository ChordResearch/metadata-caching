import dotenv from "dotenv"
dotenv.config()

import { ethers } from "hardhat";

const erc721Address: string = process.env.ERC721_ADDRESS as string;
import ERC721ABI from './erc721abi.json';

async function main() {
    const accounts = await ethers.getSigners();
    const user = accounts[0];
    const userAddress = await user.getAddress();
    const erc721Token = new ethers.Contract(erc721Address, ERC721ABI, user);
    console.log("ERC721 at:", erc721Token.address);
    // https://ipfs.io/ipfs/<cid>
    let safeMintTx = await erc721Token.safeMint(userAddress, "QmPkvqYQBSqSY5J8RwC7dbCr6xgNsVdfpZcHgx71eqZPgJ");
    let receipt = await safeMintTx.wait()
    console.log(`safe mint receipt : ${JSON.stringify(receipt.events)}`)
    receipt = receipt.events
    console.log(`minted : ${safeMintTx.hash}`)
}
// We recommend this pattern to be able to use async/await everywhere
// and properly handle errors.
main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});