package org.rebecalang.compiler.modelcompiler.hybridrebeca;

import java.util.HashMap;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.rebecalang.compiler.modelcompiler.ScopeHandler.ScopeException;
import org.rebecalang.compiler.modelcompiler.corerebeca.CoreRebecaLabelUtility;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.FormalParameterDeclaration;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Label;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.OrdinaryPrimitiveType;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.ReactiveClassDeclaration;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.SynchMethodDeclaration;
import org.rebecalang.compiler.modelcompiler.corerebeca.objectmodel.Type;
import org.rebecalang.compiler.modelcompiler.hybridrebeca.compiler.HybridRebecaCompleteParser;
import org.rebecalang.compiler.modelcompiler.hybridrebeca.objectmodel.HybridRebecaCode;
import org.rebecalang.compiler.modelcompiler.hybridrebeca.objectmodel.HybridTermPrimary;
import org.rebecalang.compiler.modelcompiler.hybridrebeca.objectmodel.ModeDeclaration;
import org.rebecalang.compiler.modelcompiler.hybridrebeca.objectmodel.PhysicalClassDeclaration;
import org.rebecalang.compiler.modelcompiler.hybridrebeca.statementsemanticchecker.expression.HybridPrimaryTermSemanticCheck;
import org.rebecalang.compiler.modelcompiler.timedrebeca.TimedRebecaCompleteCompilerFacade;
import org.rebecalang.compiler.utils.AccessModifierUtilities;
import org.rebecalang.compiler.utils.CodeCompilationException;
import org.rebecalang.compiler.utils.CompilerFeature;
import org.rebecalang.compiler.utils.ExceptionContainer;
import org.rebecalang.compiler.utils.Pair;
import org.rebecalang.compiler.utils.TypesUtilities;

public class HybridRebecaCompleteCompilerFacade extends TimedRebecaCompleteCompilerFacade {

	public final static OrdinaryPrimitiveType PHYSICAL_SYSTEM_MODE_TYPE = new OrdinaryPrimitiveType();
	public final static Label PHYSICAL_SYSTEM_MODE_LABEL = new Label();
	static {
		PHYSICAL_SYSTEM_MODE_TYPE.setName("PhysicalSystemModeType");
		PHYSICAL_SYSTEM_MODE_LABEL.setName("PhysicalSystemModeLabel");
	}

	public HybridRebecaCompleteCompilerFacade(CommonTokenStream tokens,
			Set<CompilerFeature> features, ExceptionContainer exceptionContainer) {
		super(HybridRebecaCompleteParser.class, tokens, features, exceptionContainer);
	}
	
	public HybridRebecaCompleteCompilerFacade(Class<? extends Parser> parser, CommonTokenStream tokens,
			Set<CompilerFeature> features, ExceptionContainer exceptionContainer) {
		super(parser, tokens, features, exceptionContainer);
	}
	
	protected void initialize() {
		super.initialize();
		statementSemanticCheckContainer.getExpressionSemanticCheckContainer().
		registerTranslator(HybridTermPrimary.class, new HybridPrimaryTermSemanticCheck());
	}
	
	protected void initalizeSymbolTable() {
		super.initalizeSymbolTable();
		addingPhysicalclassesToSymbolTableInInitialization();
	}
	
	protected void addingPhysicalclassesToSymbolTableInInitialization() {
		for (PhysicalClassDeclaration physicalClassDeclaration : ((HybridRebecaCode)rebecaModel.getRebecaCode()).getPhysicalClassDeclaration()) {

			if (physicalClassDeclaration.getExtends() != null) {
				CodeCompilationException rce = new CodeCompilationException(
						"Physical classes does not support inheritence",
						physicalClassDeclaration.getLineNumber(),
						physicalClassDeclaration.getCharacter());
				exceptionContainer.addException(rce);
			}

			if (physicalClassDeclaration.isAbstract()) {
				CodeCompilationException rce = new CodeCompilationException(
						"Physical classes can not be abstract",
						physicalClassDeclaration.getLineNumber(),
						physicalClassDeclaration.getCharacter());
				exceptionContainer.addException(rce);
			}

			addingAReactiveclassToSymbolTableInInitialization(physicalClassDeclaration);
			
			try {
				
				SynchMethodDeclaration method = new SynchMethodDeclaration();
				method.setName("setMode");
				FormalParameterDeclaration fpd = new FormalParameterDeclaration();
				fpd.setName("arg0");
				fpd.setType(PHYSICAL_SYSTEM_MODE_TYPE);
				method.getFormalParameters().add(fpd);
				Type type = TypesUtilities.getInstance().getType(physicalClassDeclaration.getName());
				addMethod(type, method, AccessModifierUtilities.PUBLIC, PHYSICAL_SYSTEM_MODE_LABEL);

			} catch (CodeCompilationException e) {
				e.printStackTrace();
			}
		}
	}

	protected HashMap<String, ReactiveClassDeclaration> getAllClasses() {
		HashMap<String,ReactiveClassDeclaration> allClasses = super.getAllClasses();
		for(PhysicalClassDeclaration physicalClassDeclaration : ((HybridRebecaCode)rebecaModel.getRebecaCode()).getPhysicalClassDeclaration())
			allClasses.put(physicalClassDeclaration.getName(), physicalClassDeclaration);
		return allClasses;
	}
	
	protected void fillTypeSystem() {
		
		super.fillTypeSystem();
		TypesUtilities.getInstance().addNewType(PHYSICAL_SYSTEM_MODE_TYPE);
		
		for (ReactiveClassDeclaration physicalClassDeclaration : ((HybridRebecaCode)rebecaModel.getRebecaCode()).getPhysicalClassDeclaration()) {
			if(TypesUtilities.getInstance().hasType(physicalClassDeclaration.getName())) {
				CodeCompilationException rce = new CodeCompilationException(
						"Multiple definition of "
								+ physicalClassDeclaration.getName(),
								physicalClassDeclaration.getLineNumber(),
								physicalClassDeclaration.getCharacter());
				exceptionContainer.addException(rce);
			} else {
				TypesUtilities.getInstance().addReactiveClassType(physicalClassDeclaration);			
				try {
					Type rcdType = TypesUtilities.getInstance().getType(physicalClassDeclaration.getName());
					TypesUtilities.getInstance().addTypeCompatibility(rcdType, TypesUtilities.REACTIVE_CLASS_TYPE);
				} catch (CodeCompilationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void addEnvironmentVariablesToScope() {
		super.addEnvironmentVariablesToScope();
		try {
			scopeHandler.addVariableToCurrentScope("none",
					PHYSICAL_SYSTEM_MODE_TYPE, PHYSICAL_SYSTEM_MODE_LABEL, 
					0, 0);
		} catch (ScopeException e1) {
			e1.printStackTrace();
		}
		
		for(PhysicalClassDeclaration physicalClassDeclaration : ((HybridRebecaCode)rebecaModel.getRebecaCode()).getPhysicalClassDeclaration()) {
			for(ModeDeclaration modeDeclarations : physicalClassDeclaration.getModeDeclarations()) {
				try {
					scopeHandler.addVariableToCurrentScope(modeDeclarations.getName(),
							PHYSICAL_SYSTEM_MODE_TYPE, PHYSICAL_SYSTEM_MODE_LABEL, 
							modeDeclarations.getLineNumber(), modeDeclarations.getCharacter());
				} catch (ScopeException e) {
					exceptionContainer.addException(e);
				}

			}
		}
	}
	
	protected void semanticCheckReactiveClassDeclarations() {
		super.semanticCheckReactiveClassDeclarations();
		
		for (PhysicalClassDeclaration fcd : ((HybridRebecaCode)rebecaModel.getRebecaCode())
				.getPhysicalClassDeclaration()) {

			scopeHandler.pushScopeRecord(CoreRebecaLabelUtility.REACTIVE_CLASS);

			addIntraReactiveClassVariablesToScope(fcd);
			
			semanticCheckForConstructorsOfReactiveClassDeclaration(fcd);

			semanticCheckForSynchMethodsOfReactiveClassDeclaration(fcd);

			semanticCheckForMessageServersOfReactiveClassDeclaration(fcd);

			semanticCheckForModesOfPhysicalClassDeclaration(fcd);
			
			scopeHandler.popScopeRecord();
		}
	}

	private void semanticCheckForModesOfPhysicalClassDeclaration(PhysicalClassDeclaration fcd) {
		for (ModeDeclaration md : fcd.getModeDeclarations()) {
			scopeHandler.pushScopeRecord(null);
			Pair<Type,Object> check = statementSemanticCheckContainer.getExpressionSemanticCheckContainer().check(md.getGuardDeclaration().getCondition());
			if(check.getFirst() != TypesUtilities.BOOLEAN_TYPE) {
				CodeCompilationException rce = new CodeCompilationException(
						"Conditional expression of guards should be boolean",
						md.getGuardDeclaration().getCondition().getLineNumber(), 
						md.getGuardDeclaration().getCondition().getCharacter());
				exceptionContainer.addException(rce);
			}
			statementSemanticCheckContainer.check(md.getGuardDeclaration().getBlock());
			scopeHandler.popScopeRecord();

			scopeHandler.pushScopeRecord(HybridRebecaLabelUtility.INVARIANT_BLOCK);
			check = statementSemanticCheckContainer.getExpressionSemanticCheckContainer().check(md.getInvariantDeclaration().getCondition());
			if(check.getFirst() != TypesUtilities.BOOLEAN_TYPE) {
				CodeCompilationException rce = new CodeCompilationException(
						"Conditional expression of invariants should be boolean",
						md.getInvariantDeclaration().getCondition().getLineNumber(), 
						md.getInvariantDeclaration().getCondition().getCharacter());
				exceptionContainer.addException(rce);

			}
			statementSemanticCheckContainer.check(md.getInvariantDeclaration().getBlock());
			scopeHandler.popScopeRecord();
		}
		
	}
}
