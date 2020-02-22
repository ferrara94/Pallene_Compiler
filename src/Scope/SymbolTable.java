package Scope;

import syntaxTree.Node;
import syntaxTree.expr.constant.STRING_CONST;

import java.util.*;

public class SymbolTable extends Hashtable<String, EntryInfo> {
    private String scopeName;
    private String scopeParent = null;
    private String numberLocal = "";
    private SymbolTable parentTable = null;
    private String typeNode = "unknown";

    public String getTypeNode() {
        return typeNode;
    }

    public void setTypeNode(String typeNode) {
        this.typeNode = typeNode;
    }

    public String getNumberLocal() {
        return numberLocal;
    }

    public void setNumberLocal(String numberLocal) {
        this.numberLocal = numberLocal;
    }

    public SymbolTable(String scopeName) {
        super();
        this.scopeName = scopeName;
        this.scopeParent = "";
    }

    public SymbolTable(String scopeName, String scopeParent) {
        super();
        this.scopeName = scopeName;
        this.scopeParent = scopeParent;
    }

    public boolean hasScopeParent() {
        if (this.scopeParent != null)
            return true;
        else
            return false;
    }

    public String getScopeParent() {
        if (this.hasScopeParent())
            return this.scopeParent;
        else
            return null;
    }

    public SymbolTable getParentTable() {
        return parentTable;
    }

    public void setParentTable(SymbolTable parentTable) {
        this.parentTable = parentTable;
    }

    public String getScopeName() {
        return this.scopeName;
    }

    public EntryInfo getEntryInfoByName(String name) {
        if (this.containsKey(name)) {
            return this.get(name);
        } else
            return null;
    }

    @Override
    public String toString() {
        String head = "Scope: " + this.scopeName + " - Parent: " + this.scopeParent;
        String body ="";

        for(Map.Entry <String, EntryInfo> map: this.entrySet()) {
            body += map.getKey() + " - " + map.getValue().toString() + "\n";
            //System.out.println(map.getKey());
        }

        return head + "\n" + body;
    }

    public ArrayList<String> getTypes() {
        ArrayList<String> types = new ArrayList<String>();

        for(Map.Entry <String, EntryInfo> map: this.entrySet()) {
            if( !(map.getValue().getType().equals("statement")) && !(map.getValue().getKind().equals("statement") ) ) {
                types.add(map.getValue().getType());
                //System.out.println(map.getKey()+"-"+map.getValue().getKind());
            }

        }//end for
        Collections.reverse(types);
        return types;
    }

}
