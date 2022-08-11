import mongoose from 'mongoose';

// import tokenMetadata from "../models/token-metadata";


export async function connectToDatabase(connString: string) {

    // Create a new MongoDB client with the connection string from .env
    await mongoose.connect(connString)

    mongoose.connection.on("disconnected", () => {
        console.log('disconnected');
    });

    console.log(
        `Successfully connected to database with connection string: ${connString}`,
    );
}