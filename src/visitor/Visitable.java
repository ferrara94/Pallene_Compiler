package visitor;

import Scope.SymbolTableStack;

public interface Visitable {
    Object accept(SemanticVisitor visitor);
    Object accept(CVisitor visitor);
}
