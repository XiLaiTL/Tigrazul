package `fun`.vari.tigrazul.tree

import `fun`.vari.tigrazul.action.matchingBoundType
import `fun`.vari.tigrazul.action.matchingType
import `fun`.vari.tigrazul.model.*
import `fun`.vari.tigrazul.model.Function
import `fun`.vari.tigrazul.model.Identifier.Companion.isUnknownType
import `fun`.vari.tigrazul.tree.TrigrazulParser.TO
import `fun`.vari.tigrazul.tree.TrigrazulParser.MAPSTO
import `fun`.vari.tigrazul.util.Logger
import `fun`.vari.tigrazul.util.Scope


class TigrazulMainVisitor(): TrigrazulBaseVisitor<Atom>() {
    val scope:Scope<Atom> = Scope();
    private val resourceLocation =ResourceLocation.default; //TODO: add resourceLocation for every module

    override fun visitApplication(ctx: TrigrazulParser.ApplicationContext): Atom {
        ctx.statement().forEach { visit(it) }
        return Unknown
    }

    override fun visitStatement(ctx: TrigrazulParser.StatementContext): Atom {
        return visit(ctx.declaration())
    }

    override fun visitAtom(ctx: TrigrazulParser.AtomContext): Atom {
        return super.visitAtom(ctx)
    }

    override fun visitParenAtom(ctx: TrigrazulParser.ParenAtomContext): Atom {
        return visit(ctx.atom())
    }

    override fun visitIdentifierAtom(ctx: TrigrazulParser.IdentifierAtomContext): Atom {
        if(ctx.identifier().size == 1){
            val name = ctx.identifier(0).VARIABLE().text
            val atom = scope.compute(name) { Identifier(name) }
            return atom
        }
        if(ctx.identifier().size == 2){ //TODO: 从DataSet中取出Identifier
            val name = ctx.identifier(0).VARIABLE().text
            val setName = ctx.identifier(1).VARIABLE().text
            val setAtom  = scope[setName]
            if(setAtom is Identifier){
                val setAtomValue = setAtom.value
                if(setAtomValue is DataSet){
                    return setAtomValue.get(name)?:Identifier(name)
                }
            }

        }
        throw TigrazulVisitorException at TrigrazulParser.IdentifierAtomContext::class
    }
    override fun visitTypeAtom(ctx: TrigrazulParser.TypeAtomContext): Atom {
        return Type
    }

    override fun visitPatternAtom(ctx: TrigrazulParser.PatternAtomContext): Atom {
        val patternSet = PatternSet()
        for(branchCtx in ctx.branch()){
            patternSet.add(visit(branchCtx))
        }
        return patternSet
    }

    override fun visitAssignmentAtom(ctx: TrigrazulParser.AssignmentAtomContext): Atom {
        val name = ctx.identifier().VARIABLE().text
        val identifier = Identifier(name)
        scope[name] = identifier
        if(ctx.type==null){
            val value = visit(ctx.value) //TODO: 这里要加一个建构类型的步骤
            identifier.apply {
                this.value = value
            }
        }
        else{
            val type = visit(ctx.type)
            //这里要基于type去访问value，因为这样有些参数的类型才能拿出来
            identifier.type = type //这里先用term来表示一个类型
            var value = visit(ctx.value)
            matchingBoundType(type,value)
            //TODO: 这里还得检查value的类型和type的类型匹配与否
            matchingType(type,value).onSuccess {
                value = Verified(value,it)
            }.onFailure {
                Logger.error("type different ${it.message}")
            }

            //Identifier的类型是值的类型，所以这里分离的时候会出问题
            //没法把type弄给Identifier
            identifier.apply {
                this.value = value
            };
        }
        return scope[name]!!
    }
    override fun visitTypedAtom(ctx: TrigrazulParser.TypedAtomContext): Atom {
        var last = visit(ctx.right) //这里顺序会不会有问题？？ x:x->A这种？
        if(ctx.left == null || ctx.left.isEmpty()) return last //TODO:要防止返回类型为Unknown
        for(primaryAtomCtx in ctx.left.reversed()){
            val previous = visit(primaryAtomCtx)
            if(previous.isUnknownType()) {
                (previous as Identifier).type=last
                last = previous
            }
            else {
                //TODO("这里做类型检查")
                matchingType(last,previous).onSuccess {
                    last = Verified(previous,it)
                }.onFailure {
                    Logger.error("type different ${it.message}")                }
            }
        }
        return last
    }

    override fun visitAppliedAtom(ctx: TrigrazulParser.AppliedAtomContext): Atom {
        //没有序的关系，从后往前遍历没有问题的
        var last:Atom = visit(ctx.right)
        if(ctx.left == null || ctx.left.isEmpty()) return last
        for(typedAtomCtx in ctx.left.reversed()){
            val previous = visit(typedAtomCtx)
            if(previous.isUnknownType()){
                val lastType= last.type
                if( lastType is Function){
                    (previous as Identifier).type = lastType.current //这里做一次类型推导
                }
                //TODO: 这里要不要把lastType给解构了？？不然链会有问题？
            }
            last = Applied(previous,last)
            //TODO: 这里类型怎么办呢？
        }
        return last
    }
    //TODO: 现在的问题是，如果已知 p q 要变成 \lambda p.p q的时候，p和后面的p有绑定关系，这个怎么办？
    override fun visitFunctionAtom(ctx: TrigrazulParser.FunctionAtomContext): Atom {
        if(ctx.left == null || ctx.left.isEmpty()) return visit(ctx.right)
        val atomList = mutableListOf<Atom>()
        for(i in 0 until ctx.op.size){
            scope.pushStack()
            atomList+=visit(ctx.left[i])
        }
        var last = visit(ctx.right)
        for(i in ctx.op.size-1 downTo  0 ){
            scope.popStack()
            last = when(ctx.op[i].type){
                TO -> Function(atomList[i],last)
                MAPSTO -> MapsToFunction(atomList[i],last)
                else -> throw TigrazulVisitorException at TrigrazulParser.FunctionAtomContext::class
            }
            //TODO: 这里类型怎么办呢？而且要不要顺便处理一下同名的问题？要不要把Identifier换成bound？
            //由于有前后绑定关系，是不是还是得先前面再后面的遍历方式？
        }
        return last
    }


    override fun visitTermDeclaration(ctx: TrigrazulParser.TermDeclarationContext): Atom {
        val name = ctx.identifier().VARIABLE().text
        scope[name] = Identifier(name).apply { type = visit(ctx.atom()) }
        return scope[name]!!
    }

    override fun visitConstructorDeclaration(ctx: TrigrazulParser.ConstructorDeclarationContext): Atom {
        val name = ctx.identifier().VARIABLE().text
        val dataSet = DataSet()
        val identifier = Identifier(name)
        scope[name] = identifier
        for(constructorCtx in ctx.constructor()){
            val constructorName = constructorCtx.identifier().VARIABLE().text
            val type = if(constructorCtx.atom()!=null && !constructorCtx.atom().isEmpty) visit(constructorCtx.atom())
                else identifier
            dataSet.add(Identifier(constructorName).apply { this.type = type })
        }
        identifier.apply {
            value = dataSet
        }

        return scope[name]!!
    }

    override fun visitAssignmentDeclaration(ctx: TrigrazulParser.AssignmentDeclarationContext): Atom {
        return visit(ctx.assignmentAtom())
    }



    override fun visitBranch(ctx: TrigrazulParser.BranchContext): Atom {
        return visit(ctx.atom())
    }
//
//    override fun visitConstructor(ctx: TrigrazulParser.ConstructorContext): Atom {
//        return super.visitConstructor(ctx)
//    }
//
//    override fun visitIdentifier(ctx: TrigrazulParser.IdentifierContext): Atom {
//        return super.visitIdentifier(ctx)
//    }
}