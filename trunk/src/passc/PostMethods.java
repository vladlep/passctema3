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
		    
		    if(rezultat >50 && (lungimeSir ==0 || (this.nrConex % lungimeSir) ==0 )) 
	    	{	    		
	    
				 IMetaInterception pIMeta = (IMetaInterception) this.pIOCM.QueryInterface("OpenCOM.IMetaInterception");
		    	// 1. creem cele 2 noi componente
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
		        IDelegator pDel08 = pIMeta.GetDelegator(pDivOpIUnk, "passc.INb");
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
		
	    if( (method.equals("eval") || method.equals("getNb")))
		{
	    	this.nrConex ++;
	    	int rezultat = ((Integer)result).intValue();
	    	
	    	
	    	
	    	//vrem doar 1 data sa modificam nu de fiecare data cand intra
	    	Vector<Long> sir = new Vector<Long>();
	    	IMetaArchitecture pIMetaArchitect = (IMetaArchitecture)this.pIOCM.QueryInterface("OpenCOM.IMetaArchitecture");
		    int lungimeSir = pIMetaArchitect.enumConnsToIntf(this.pIUnk, "passc.INb", sir);	    	
		    
		    System.out.println("ConstantChange : rezultat "+rezultat +" lungime sir "+lungimeSir +" nr conexiuni "+ nrConex);
		    
		    if(rezultat >10 && (lungimeSir ==0 || (this.nrConex % lungimeSir) ==0 )) 
	    	{	    		
		    	 
		    	IMetaInterception pIMeta = (IMetaInterception) this.pIOCM.QueryInterface("OpenCOM.IMetaInterception");
		    	// 1. creem noua componenta

				// Create the SubOp component
		        IUnknown pSubOpIUnk = (IUnknown) pIOCM.createInstance("passc.SubOp", "Sub"+uniqueId);
		        uniqueId++;
		        ILifeCycle pILife = (ILifeCycle) pSubOpIUnk.QueryInterface("OpenCOM.ILifeCycle");
		        pILife.startup(pIOCM);
		        //add intereceptors
		        IDelegator pDel08 = pIMeta.GetDelegator(pSubOpIUnk, "passc.INb");
		        PostMethods Interceptors = new PostMethods(pIOCM,pSubOpIUnk);
		        pDel08.addPostMethod(Interceptors, "SubChange");
		        
		        //2.conectam la sub cele 2 componente A si B
		        Vector<Long> lista = new Vector<Long>();
		        pIMetaArchitect.enumConnsFromRecp(this.pIUnk,"passc.INb", lista);
			    
		        
			        this.pIOCM.connect(pSubOpIUnk, this.pIOCM.getConnectionInfo(lista.get(0)).sinkComponent, "passc.INb");
			        this.pIOCM.connect(pSubOpIUnk, this.pIOCM.getConnectionInfo(lista.get(1)).sinkComponent, "passc.INb");
			        
			        //3. aflam de cine era legata veche componenta, legam la sub si deconectam
			    
			  
			        long aux;
			        int i;
			        for( i=0; i< lungimeSir; i++)
			        {
			        	aux = sir.get(i);
			        	OCM_ConnInfo_t connInfo = this.pIOCM.getConnectionInfo(aux);
			        	this.pIOCM.connect(connInfo.sourceComponent, pSubOpIUnk, "passc.INb");
			        	// problema e ca a adaugat noua conexiune la sf listei si ea putea fi chiar prima
			        	
			        	lista = new Vector<Long>();
						pIMetaArchitect.enumConnsFromRecp(connInfo.sourceComponent, "passc.Inb", lista);
						  if(lista.size() >1) // adica nu e sub nodul radacina
						    {    
								//verific daca prima componenta din lista este egala cu cea veche 
								if(this.pIOCM.getConnectionInfo(lista.get(0)).sinkComponentName.equals(connInfo.sinkComponentName)) 
								{
									//daca da vom deconecta pe a 2-a si o reconectam la sfarit (a 3-a) , a.i. componenta nou introdusa avanseaza 1 pozitie
									IUnknown auxiliar = this.pIOCM.getConnectionInfo(lista.get(1)).sinkComponent;
									this.pIOCM.disconnect(lista.get(1));
									this.pIOCM.connect(connInfo.sourceComponent, auxiliar, "passc.INb");
								}
				
								 this.pIOCM.disconnect(aux);
								 this.pIOCM.deleteInstance(connInfo.sinkComponent);						        
						    }
						  else
				        	if(lista.size() ==1 ) //e nodul radacina
				        	{
				        		System.out.println("Schimbam primul nod de sub radacina in AddChange");
				        		this.pIOCM.disconnect(sir.get(0));
								this.pIOCM.connect(TestOP.pINb,pSubOpIUnk,"passc.INb");
								this.pIOCM.deleteInstance(pIUnk);
				        	}
			        }
	    	}   
		}
		return result;
		
	}
	
	public Object DivChange(String method, Object result, Object[] args,Exception e)
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
		    
		    if(rezultat >10 && (lungimeSir ==0 || (this.nrConex % lungimeSir) ==0 )) 
	    	{	    		
		    	
		        //2.conectam la sub cele 2 componente A si B
		        Vector<Long> lista = new Vector<Long>();
		        pIMetaArchitect.enumConnsFromRecp(this.pIUnk,"passc.INb", lista);
				IUnknown auxiliar = this.pIOCM.getConnectionInfo(lista.get(0)).sinkComponent;
		        this.pIOCM.disconnect(lista.get(0));
		        this.pIOCM.connect(pIUnk, auxiliar, "passc.INb");
	        
	    	}
	        
		}
		     
		return result;
	}
	
	public Object MulChange(String method, Object result, Object[] args,Exception e)
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
			    
			    if(rezultat >10 && (lungimeSir ==0 || (this.nrConex % lungimeSir) ==0 )) 
		    	{	    		
			    	 
			    	IMetaInterception pIMeta = (IMetaInterception) this.pIOCM.QueryInterface("OpenCOM.IMetaInterception");
			    	// 1. creem noile componenta : 2 * Sub

					// Create the SubOp component
			        IUnknown pSubOpIUnk = (IUnknown) pIOCM.createInstance("passc.SubOp", "Sub"+uniqueId);
			        uniqueId++;
			        ILifeCycle pILife = (ILifeCycle) pSubOpIUnk.QueryInterface("OpenCOM.ILifeCycle");
			        pILife.startup(pIOCM);
			        //add intereceptors
			        IDelegator pDel08 = pIMeta.GetDelegator(pSubOpIUnk, "passc.INb");
			        PostMethods Interceptors = new PostMethods(pIOCM,pSubOpIUnk);
			        pDel08.addPostMethod(Interceptors, "SubChange");

			        IUnknown pSubOpIUnk2 = (IUnknown) pIOCM.createInstance("passc.SubOp", "Sub"+uniqueId);
			        uniqueId++;
			        pILife = (ILifeCycle) pSubOpIUnk2.QueryInterface("OpenCOM.ILifeCycle");
			        pILife.startup(pIOCM);
			        //add intereceptors
			        IDelegator pDel09 = pIMeta.GetDelegator(pSubOpIUnk2, "passc.INb");
			        Interceptors = new PostMethods(pIOCM,pSubOpIUnk2);
			        pDel09.addPostMethod(Interceptors, "SubChange");

			        
			        //2.1.conectam la sub cele 2 componente A si B
			        Vector<Long> lista = new Vector<Long>();
			        pIMetaArchitect.enumConnsFromRecp(this.pIUnk,"passc.INb", lista);
			        this.pIOCM.connect(pSubOpIUnk, this.pIOCM.getConnectionInfo(lista.get(0)).sinkComponent, "passc.INb");
			        this.pIOCM.connect(pSubOpIUnk, this.pIOCM.getConnectionInfo(lista.get(1)).sinkComponent, "passc.INb");
			        //2.2.conectam la sub2 cele 2 componente sub si B
			        this.pIOCM.connect(pSubOpIUnk2, pSubOpIUnk, "passc.INb");
			        this.pIOCM.connect(pSubOpIUnk2, this.pIOCM.getConnectionInfo(lista.get(1)).sinkComponent, "passc.INb");
			        
			        //3. aflam de cine era legata veche componenta, legam la sub2 si deconectam
			    
			        
			        long aux;
			        int i;
			        for( i=0; i< lungimeSir; i++)
			        {
			        	aux = sir.get(i);
			        	OCM_ConnInfo_t connInfo = this.pIOCM.getConnectionInfo(aux);
			        	
			        	
			        	lista = new Vector<Long>();
						pIMetaArchitect.enumConnsFromRecp(connInfo.sourceComponent, "passc.Inb", lista);
						if(lista.size()>1) // nu e nod radacina
						{
						
							
				        	// problema e ca a adaugat noua conexiune la sf listei si ea putea fi chiar prima
							
							//verific daca prima componenta din lista este egala cu cea veche 
								if(this.pIOCM.getConnectionInfo(lista.get(0)).sinkComponentName.equals(connInfo.sinkComponentName) ) 
								{
									this.pIOCM.connect(connInfo.sourceComponent, pSubOpIUnk2, "passc.INb");
									//daca da vom deconecta pe a 2-a si o reconectam la sfarit (a 3-a) , a.i. componenta nou introdusa avanseaza 1 pozitie
				
									IUnknown auxiliar = this.pIOCM.getConnectionInfo(lista.get(1)).sinkComponent;
									this.pIOCM.disconnect(lista.get(1));
									this.pIOCM.connect(connInfo.sourceComponent, auxiliar, "passc.INb");
								}
						 }
						 else
								if(lista.size() ==1) //este radacina root
							{
									System.out.println("Schimbam primul nod de sub radacina");
									this.pIOCM.disconnect(lista.get(0));
									this.pIOCM.connect(TestOP.pINb,pSubOpIUnk2,"passc.INb");
							}
						 this.pIOCM.disconnect(aux);
						 this.pIOCM.deleteInstance(connInfo.sinkComponent);	       	     
			        }   
		    	}
			}
		return result;
	}
	
	public Object SubChange(String method, Object result, Object[] args,Exception e)
	{
		return result;
	}
}
