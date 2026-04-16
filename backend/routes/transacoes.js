const express = require('express');
const router  = express.Router();
const pool    = require('../db');

// Listar todas as transações
router.get('/', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM transacoes ORDER BY id DESC');
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ erro: err.message });
  }
});

// Adicionar transação
router.post('/', async (req, res) => {
  const { tipo, descricao, valor } = req.body;
  if (!tipo || !descricao || !valor) {
    return res.status(400).json({ erro: 'Campos obrigatórios: tipo, descricao, valor' });
  }
  try {
    const result = await pool.query(
      'INSERT INTO transacoes (tipo, descricao, valor) VALUES ($1, $2, $3) RETURNING *',
      [tipo, descricao, valor]
    );
    res.status(201).json(result.rows[0]);
  } catch (err) {
    res.status(500).json({ erro: err.message });
  }
});

// Deletar transação
router.delete('/:id', async (req, res) => {
  try {
    await pool.query('DELETE FROM transacoes WHERE id = $1', [req.params.id]);
    res.json({ mensagem: 'Transação removida.' });
  } catch (err) {
    res.status(500).json({ erro: err.message });
  }
});

module.exports = router;
