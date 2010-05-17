package passc;

import java.util.Vector;

import OpenCOM.IDelegator;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaArchitecture;
import OpenCOM.IMetaInterception;
import OpenCOM.IOpenCOM;
import OpenCOM.IUnknown;
import OpenCOM.OCM_ConnInfo_t;

public class PostMethods 
{
	private IOpenCOM pIOCM;
	private IUnknown pIUnk;
	private int nrConex =0;
	private static int uniqueId = 0;
	
	public PostMethods(IOpenCOM pIOCM, IUnknown pIUnk) {
	
	this.pIOCM = pIOCM;
	this.pIUnk = pIUnk;
	this.nrConex = 0;
}


	

	public Object ConstantChange(String method, Object result, Object[] args,Exception e)
	{
		
	    if( (method.equals("eval") || method.equals("getNb")))
		{
	    	this.nrConex ++;
	    	int rezultat = ((Integer)result).intValue();
	    	
	    	
	    	
	    	//vrem doar 1 data sa modificam nu de fiecare data cand intra
	    	Vector<Long> sir = new Vector<Long>();
	    	IMetaArchitecture pIMetaArchitect = (IMetaArchitecture)this.pIOCM.QueryInterface("OpenCOM.IMetaArchitecture");
		    int lungimeSir = pIMetaArchitect.enumConnsToIntf(this.pIUnk, "passc.INb", sir);	    	
		    
		    System.out.println("ConstantChange : rezultat "+rezultat +" lungime sir "+lungimeSir +" nr conexiuni "+ nrConex);
		    
		    if(rezultat >50 && (lungimeSir ==0 || nrConex % lungimeSir ==0 )) 
	    	{	    		
	    	
		    	// 1. creem cele 2 noi componente
							    
				 IMetaInterception pIMeta = (IMetaInterception) this.pIOCM.QueryInterface("OpenCOM.IMetaInterception");
		        //Create constant component
				
		        IUnknown pNbSourceIUnk = (IUnknown) pIOCM.createInstance("passc.NbSource", "constanta10");
		        ILifeCycle pILife = (ILifeCycle) pNbSourceIUnk.QueryInterface("OpenCOM.ILifeCycle");
		        pILife.startup(pIOCM);
		     
		       // System.out.println(pNbSourceIUnk.QueryInterface("passc.IConstant"));
		        ((IConstant)pNbSourceIUnk.QueryInterface("passc.IConstant")).setVal(10);
		        System.out.println("s-a ajuns aici");
		        
				// Create the DivOp component
		        IUnknown pDivOpIUnk = (IUnknown) pIOCM.createInstance("passc.DivOp", "Div"+uniqueId);
		        uniqueId++;
		        pILife = (ILifeCycle) pDivOpIUnk.QueryInterface("OpenCOM.ILifeCycle");
		        pILife.startup(pIOCM);
		        //add intereceptors
		        IDelegator pDel08 = pIMeta.GetDelegator(pNbSourceIUnk, "passc.INb");
		        PostMethods Interceptors = new PostMethods(pIOCM,pDivOpIUnk);
		        pDel08.addPostMethod(Interceptors, "DivChange");
		        
		        //2.conectam la divizor fosta componenta si constanta 10
		        this.pIOCM.connect(pDivOpIUnk, this.pIUnk, "passc.INb");
		        this.pIOCM.connect(pDivOpIUnk, pNbSourceIUnk, "passc.INb");
		        
		        //3. aflam de cine era legata veche componenta, legam la divizor si deconectam
		    
		        
		        long aux;
		        int i;
		        for( i=0; i< lungimeSir; i++)
		        {
		        	aux = sir.get(i);
		        	OCM_ConnInfo_t connInfo = this.pIOCM.getConnectionInfo(aux);
		        	this.pIOCM.connect(connInfo.sourceComponent, pDivOpIUnk, "passc.INb");
		        	// problema e ca a adaugat noua conexiune la sf listei si ea putea fi chiar prima
		        	
		        	Vector<Long> lista = new Vector<Long>();
					pIMetaArchitect.enumConnsFromRecp(connInfo.sourceComponent, "passc.Inb", lista);
					
					//verific daca prima componenta din lista este egala cu cea veche 
					if(this.pIOCM.getConnectionInfo(lista.get(0)).sinkComponentName.equals(connInfo.sinkComponentName)) 
					{
						//daca da vom deconecta pe a 2-a si o reconectam la sfarit (a 3-a) , a.i. componenta nou introdusa avanseaza 1 pozitie
						IUnknown auxiliar = this.pIOCM.getConnectionInfo(lista.get(1)).sinkComponent;
						this.pIOCM.disconnect(lista.get(1));
						this.pIOCM.connect(connInfo.sourceComponent, auxiliar, "passc.INb");
					}
	
					 this.pIOCM.disconnect(aux);
		        }
		     
	    	}   
		}
		return result;
		
	}
	
	public Object AddChange(String method, Object result, Object[] args,Exception e)
	{
		return result;
	}
	
	public Object DivChange(String method, Object result, Object[] args,Exception e)
	{
		return result;
	}
	
	public Object MulChange(String method, Object result, Object[] args,Exception e)
	{
		return result;
	}
	
	public Object SubChange(String method, Object result, Object[] args,Exception e)
	{
		return result;
	}
}
