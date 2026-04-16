const express = require('express');
const cors    = require('cors');
require('dotenv').config();

const transacoesRoutes = require('./routes/transacoes');

const app = express();
app.use(cors());
app.use(express.json());

app.use('/api/transacoes', transacoesRoutes);

app.get('/', (req, res) => res.send('Backend Controle de Finanças rodando!'));

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Servidor rodando na porta ${PORT}`));
