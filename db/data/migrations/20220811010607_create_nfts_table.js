/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function (knex) {
    return knex.schema.createTable('nfts', (table) => {
        table.increments();
        table.string('tokenAddress').notNullable()
        table.string('tokenId').notNullable();
        table.string('tokenURI', 50000);
        table.string('minterAddress').notNullable()
        table.bigInteger('blockNumber').notNullable();
        table.string('transactionHash').notNullable()
        table.boolean('status').defaultTo(true);
        table.timestamp('createdAt').defaultTo(knex.fn.now())
        table.timestamp('updatedAt').defaultTo(knex.fn.now())

        table.primary(['tokenAddress', 'tokenId'])
    })
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function (knex) {
    return knex.schema.dropTable('nfts')
};
