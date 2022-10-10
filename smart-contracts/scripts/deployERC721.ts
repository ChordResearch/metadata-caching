import { ethers } from "hardhat";

async function main() {
    const MySuperNFTsContract = await ethers.getContractFactory("MySuperNFTs");
    const MySuperNFTs = await MySuperNFTsContract.deploy();
    await MySuperNFTs.deployed();
    console.log("MySuperNFTs token with storage deployed to:", MySuperNFTs.address);
}

// We recommend this pattern to be able to use async/await everywhere
// and properly handle errors.
main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});
