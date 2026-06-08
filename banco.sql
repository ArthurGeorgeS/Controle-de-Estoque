-- =====================================================
--  Script SQL — Sistema Integrado APOO + Banco de Dados
--  Banco: SQLite  |  Encoding: UTF-8
-- =====================================================

-- ─────────────────────────────────────
--  Exercício 1 — Cadastro de Clientes
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS clientes (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    nome          TEXT    NOT NULL,
    cpf           TEXT    NOT NULL UNIQUE,   -- unicidade garantida no banco
    email         TEXT,
    telefone      TEXT,
    data_cadastro TEXT    NOT NULL            -- formato ISO: YYYY-MM-DD
);

-- ─────────────────────────────────────
--  Exercício 2 — Controle de Estoque
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS categorias (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nome      TEXT NOT NULL,
    descricao TEXT
);

CREATE TABLE IF NOT EXISTS produtos (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    nome         TEXT    NOT NULL,
    preco        REAL    NOT NULL,
    quantidade   INTEGER NOT NULL DEFAULT 0,
    categoria_id INTEGER NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- ─────────────────────────────────────
--  Exercício 3 — Agendamento
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS pacientes (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome     TEXT NOT NULL,
    cpf      TEXT NOT NULL UNIQUE,
    telefone TEXT
);

CREATE TABLE IF NOT EXISTS medicos (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    nome          TEXT NOT NULL,
    crm           TEXT NOT NULL UNIQUE,
    especialidade TEXT
);

CREATE TABLE IF NOT EXISTS consultas (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_id INTEGER NOT NULL,
    medico_id   INTEGER NOT NULL,
    data_hora   TEXT    NOT NULL,                   -- ISO 8601: YYYY-MM-DDTHH:MM
    status      TEXT    NOT NULL DEFAULT 'AGENDADA', -- AGENDADA | CANCELADA
    observacoes TEXT,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (medico_id)   REFERENCES medicos(id)
);

-- ─────────────────────────────────────
--  Exercício 4 — Sistema Bancário
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS correntistas (
    id    INTEGER PRIMARY KEY AUTOINCREMENT,
    nome  TEXT NOT NULL,
    cpf   TEXT NOT NULL UNIQUE,
    email TEXT
);

CREATE TABLE IF NOT EXISTS contas (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_conta    TEXT    NOT NULL UNIQUE,
    saldo           REAL    NOT NULL DEFAULT 0,
    correntista_id  INTEGER NOT NULL,
    FOREIGN KEY (correntista_id) REFERENCES correntistas(id)
);

CREATE TABLE IF NOT EXISTS extrato (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    conta_id       INTEGER NOT NULL,
    tipo_operacao  TEXT    NOT NULL,  -- DEPOSITO | SAQUE
    valor          REAL    NOT NULL,
    data_hora      TEXT    NOT NULL,  -- ISO 8601 com hora
    FOREIGN KEY (conta_id) REFERENCES contas(id)
);

-- ─────────────────────────────────────
--  Exercício 5 — Avaliação de Funcionários
-- ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS departamentos (
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS funcionarios (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    nome             TEXT    NOT NULL,
    cargo            TEXT,
    departamento_id  INTEGER NOT NULL,
    FOREIGN KEY (departamento_id) REFERENCES departamentos(id)
);

CREATE TABLE IF NOT EXISTS avaliacoes (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    funcionario_id  INTEGER NOT NULL,
    nota            REAL    NOT NULL CHECK(nota >= 0 AND nota <= 10),
    comentario      TEXT,
    data            TEXT    NOT NULL,   -- YYYY-MM-DD
    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
);
