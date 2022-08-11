import mongoose, { Schema, Document } from "mongoose";

const metadataSchema: Schema = new Schema({
    fullname: {
        type: String
    },
    address: {
        type: String
    },
    assetCDNUrl: {
        type: String
    },
    metadata: {
        type: Schema.Types.Mixed
    }
}, { strict: false, collection: 'tokenMetadata' });

const Metadata = mongoose.model("tokenMetadata", metadataSchema, 'tokenMetadata');
export default Metadata;