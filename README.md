# 🧱 Breakout - Jogo em Java com Swing

Projeto de um jogo estilo *Breakout* desenvolvido em Java, utilizando `Canvas`, `Thread`, `KeyListener` e `BufferStrategy`. O jogo consiste em controlar uma raquete para rebater a bola e quebrar blocos.

---

## 🎮 Funcionalidades

- Movimento da raquete com teclas **← / →** e **A / D**
- Bola com colisão simples
- Lógica de movimentação baseada em `Thread`
- Interface gráfica feita com Java `AWT` e `Swing`
- Estrutura modularizada por classes

---

## 🧠 Objetivos didáticos

Este projeto foi desenvolvido com foco em:

- Prática de **Java 2D com Swing**
- Aplicação de **threads** e controle de tempo
- Estruturação em **classes e pacotes**
- Uso de **eventos de teclado**
- Conceitos de **sincronização com Mutex**

---

## 🗂️ Estrutura do projeto

```
/src
 ├── Main.java                 # Inicialização da janela
 ├── Jogo.java                 # Loop principal e renderização
 ├── Bola.java                 # Lógica da bola
 ├── RaqueteJogador.java       # Lógica da raquete
 ├── InterrupcaoTeclado.java   # Leitura do teclado
 ├── audio                     # Sons do Jogo
 ├── fonts                     # Fontes usadas
        └── atari.font            # Fonte Atari
 └── images                    # Imagens do jogo
        ├── bola.png              # Bola do jogo
        ├── bloco.png             # Blocos do jogo
        └── raquete.png           # Raquete do jogo
```

---

## ▶️ Como executar

### Pré-requisitos

- JDK 21 ou superior
- IDE Java (Eclipse, IntelliJ, VS Code) ou terminal com `javac`

### Compilando e executando via terminal:

```bash
javac *.java
java Main
```

---

## 💡 Próximos passos (ideias de melhoria)

- Adicionar pontuação
- Criar níveis com blocos
- Inserir efeitos sonoros
- Colisões mais realistas (ângulos)
- Sistema de vidas

---

## 🧑‍💻 Autor

Projeto desenvolvido por **Gabryel Henrik**, como parte de estudos em Java e aplicação de pensamento lógico com interfaces gráficas.

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License** – veja o arquivo [LICENSE](./LICENSE) para detalhes.
