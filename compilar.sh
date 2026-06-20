#!/bin/bash
# Compila o projeto e executa o P1 (e opcionalmente o P2).
# Uso: ./compilar.sh        -> compila e roda só o P1
#      ./compilar.sh p2     -> compila, roda o P1 e depois abre o P2 (GUI)

ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/src"
BIN="$ROOT/bin"

echo "=== Compilando ==="
mkdir -p "$BIN"

javac -d "$BIN" \
  "$SRC/excecoes/DadoInvalidoException.java" \
  "$SRC/excecoes/RegistroNaoEncontradoException.java" \
  "$SRC/modelo/Identificavel.java" \
  "$SRC/modelo/PessoaAbstrata.java" \
  "$SRC/modelo/Consulta.java" \
  "$SRC/modelo/Paciente.java" \
  "$SRC/modelo/Medico.java" \
  "$SRC/modelo/BaseDados.java" \
  "$SRC/leitura/LeitorCsv.java" \
  "$SRC/leitura/GravadorBinario.java" \
  "$SRC/busca/BuscaMedico.java" \
  "$SRC/busca/BuscaPaciente.java" \
  "$SRC/busca/EscritorResultados.java" \
  "$SRC/ui/TelaInicial.java" \
  "$SRC/ui/TelaMedico.java" \
  "$SRC/ui/TelaPaciente.java" \
  "$SRC/p1/MainP1.java" \
  "$SRC/p2/MainP2.java"

if [ $? -ne 0 ]; then echo "[ERRO] Falha na compilação."; exit 1; fi

echo "=== OK — Rodando P1 ==="
cd "$ROOT"
java -cp "$BIN" p1.MainP1

if [ "$1" == "p2" ]; then
  echo "=== Abrindo P2 (interface gráfica) ==="
  java -cp "$BIN" p2.MainP2
fi
