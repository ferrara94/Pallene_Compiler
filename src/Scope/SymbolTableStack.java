package Scope;

import syntaxTree.Node;

import java.util.ArrayList;

public class SymbolTableStack implements Stack<SymbolTable>{

    private ArrayList<SymbolTable> symbolTable;

    public ArrayList<SymbolTable> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(ArrayList<SymbolTable> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public ArrayList<SymbolTable> getStack() {
        return this.symbolTable;
    }

    public SymbolTableStack() {
        this.symbolTable = new ArrayList<SymbolTable>();
    }

    public int getSize() {
        return this.symbolTable.size();
    }

    @Override
    public void push(SymbolTable symbolTable) {
        this.symbolTable.add(symbolTable);
    }

    @Override
    public SymbolTable pop() {
        return this.symbolTable.remove(symbolTable.size() - 1);
    }

    @Override
    public SymbolTable top() {
        return this.symbolTable.get(symbolTable.size() - 1);
    }

    @Override
    public boolean isEmpty() {
        return this.symbolTable.isEmpty();
    }

    public boolean isContained(String scope) {
        for(SymbolTable t: this.symbolTable) {
            if (t.getScopeName().equals(scope))
                return true;
        }
        return false;
    }

    public SymbolTable getSymbolTableByNLocal(String numberLocal) {
        for(SymbolTable t: this.symbolTable) {
            if(t.getNumberLocal().equals(numberLocal))
                return t;
        }
        return null;
    }

    public SymbolTable getSymbolTableByName(String scopeName) {
        for(SymbolTable t: this.symbolTable) {
            if (t.getScopeName().equals(scopeName))
                return t;
        }
        return null;
    }

    public SymbolTable getSymbolTableByType(String scopeName, int type) {
        for(SymbolTable t: this.symbolTable) {
            System.out.println(t.getScopeName());
            String intType = Integer.toString(type);
            EntryInfo e = t.getEntryInfoByName(scopeName);

            if (e.getType().equals(intType)) {
                return t;
            }
        }
        return null;
    }


    public String toString() {
        String result ="";
        for (SymbolTable s: this.symbolTable)
            result += s.toString() + "\n";
        return result;
    }

    public boolean removeTable(String scope) {
        if(!this.isContained(scope)) {
            //System.err.println("elemento non presente");
            return false;
        } else {
            int i=0;
            while (i < this.symbolTable.size()) {

                SymbolTable s = this.symbolTable.get(i);
                if (s.getScopeName().equals(scope)){
                    this.symbolTable.remove(i);
                    return true;
                }
                i++;

            }
            return false;
        }
    }

}
