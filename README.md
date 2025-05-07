# ⚙️🧬 myPallene Compiler
## Overview

**myPallene** is a custom-built compiler for a variant of the Pallene programming language — a statically-typed, ahead-of-time-compiled sister language to Lua. It emphasizes performance and simplicity, drawing inspiration from Titan while introducing custom design elements and tooling.

This compiler implements the core stages of compilation: Lexical Analysis, Syntax Parsing, Semantic Checking, and Intermediate Code Generation, providing a robust foundation for language processing and execution.

**Curious about how languages work under the hood?** 💡 Dive into the internals with myPallene!

---
## Features

- 🔤 Lexical Analysis via JFlex
- 🧠 Syntax Parsing with JavaCup (LR-based)
- ✅ Semantic Analysis for type and scope checks
- 🧱 Intermediate Code Generation 
- 🧭 Implements the Visitor Pattern for AST traversal
- 🛠️ Written in Java SE with C extensions

---

## 📝 Instructions & Set-up

#### JavaCup
To use JavaCUP and JFlex, ensure their respective **.jar** files are present in the project directory.

#### IntelliJ IDEA Configuration
To integrate JFlex and JavaCUP into your IntelliJ project:
- Navigate to File → Project Structure → Modules
- Click the + button to add the .jar files as module dependencies

📁 For a more detailed guide (in **Italian**), see the srcjflexcup directory, which contains complete setup instructions.
	

---

## Technical specifications

    See the "t_specifications" read.me file in the directory.
---

## Technologies & Concepts
- Languages: Java SE, C
- Tools: JFlex, JavaCup
- Design Patterns: Visitor Pattern, LR Parsing
- Architecture: Modular compiler pipeline for clear phase separation

📁 These file are located in **srcjflexcup** directory. 

---

## 🌳 Syntax Tree

- The Syntax Tree is constructed using LR parsing concepts.
- Java classes from the **javax.xml.parsers** library are used during this phase.
- All grammar node types are defined in the **syntaxTree package**.
- The main class responsible for building the syntax tree is **TesterParser**.

---

## 🔍 Semantic Analysis & C Translation

- Semantic analysis and C translation are implemented using the **Visitor** Design Pattern applied to the syntax tree.
- The **Scope package** contains supporting classes such as **SymbolTable** and TypeSystem.
- The **TesterSemantic** class performs semantic checks.
- The **TesterC** class handles translation from the syntax tree to C code.

----

## Visitor Pattern

#### The visitor package includes the core interfaces and classes for the visitor pattern implementation:
- Visitable
- Visitor: package which contains the necessary classes and interfaces to implement the visitor pattern
- SemanticVisitor
- CVisitor

---

## 🧪 Test Files
	
	° file txt in "test_file" directory. 








