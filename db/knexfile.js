// Update with your config settings.

const pgLocal = {
  host: 'localhost',
  port: 5432,
  database: 'ethseoul-db',
  user: 'root',
  password: 'rootpassword'
}

/**
 * @type { Object.<string, import("knex").Knex.Config> }
 */
module.exports = {
  development: {
    client: 'pg',
    connection: {
      host: pgLocal.host,
      port: pgLocal.port,
      database: pgLocal.database,
      user: pgLocal.user,
      password: pgLocal.password,
      timezone: 'Asia/Singapore'
    },
    pool: {
      min: 2,
      max: 10
    },
    migrations: {
      directory: './data/migrations'
    },
    seeds: {
      directory: './data/seeds'
    }
  }

};
