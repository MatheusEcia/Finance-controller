const pool = require('./db');

async function setup() {
  await pool.query(`
    CREATE TABLE IF NOT EXISTS transacoes (
      id        SERIAL PRIMARY KEY,
      tipo      VARCHAR(10)     NOT NULL,
      descricao VARCHAR(255)    NOT NULL,
      valor     DECIMAL(10, 2)  NOT NULL,
      criado_em TIMESTAMP       DEFAULT NOW()
    );
  `);
  console.log('Tabela criada com sucesso!');
  process.exit();
}

setup().catch(err => { console.error(err); process.exit(1); });
