package hlmp.CommLayer;
import java.net.InetAddress;
import java.util.UUID;

import android.util.Log;
import hlmp.CommLayer.Constants.CommunicationQuality;
import hlmp.CommLayer.Constants.NetUserQuality;
import hlmp.NetLayer.NetData;



/**
 * Datos de usuario dentro de la MANET
 */
public class NetUser {
	
	private String nameTagLog = "NetUser";

	/**
     * Id del usuario, esta variable se serializa
     */
    private UUID id;

    /**
     * Nombre del usuario, esta variable se serializa
     */
    private String name;

    /**
     * Dirección IP, esta variable se serializa
     */
    private InetAddress ip;

    /**
     * Array con los id's de la vecindad TCP, esta variable se serializa
     */
    private UUID[] neighborhoodIds;

    /**
     * La calidad de la señal, NO se serializa
     */
    private int signalQuality;

    /**
     * Valor de señal restante, NO se serializa
     */
    private int timeout;

    /**
     * Saltos UDP que se encuentra del usuario remoto, NO se serializa
     */
    private int jumpsAway;

    /**
     * Tiempo de espera restante para intentar una conexion TCP con este usuario remoto, NO se serializa
     */
    private int waitTimeOut;

    /**
     * El estado de la señal
     */
    private int state;

    /**
     * El grupo al que pertenece
     */
    private byte[] upLayerData;
    
    /**
     * Default Constructor
     */
    public NetUser() {
    	this.name = "undef";
//        try {
//			this.name = InetAddress.getLocalHost().getHostName();
//		} catch (UnknownHostException e) {}
        this.signalQuality = NetUserQuality.NORMAL;
        this.neighborhoodIds = new UUID[0];
        this.jumpsAway = 0;
        this.waitTimeOut = 0;
        this.state = CommunicationQuality.NORMAL;
        this.upLayerData = new byte[0];
    }
    
    /**
     * Constructor parametrizado, para uso interno, no se debe usar directamente
     * @param id El id del usuario
     * @param name El nombre del usuario
     * @param ip La dirección IP del usuario
     * @param neighborhood Un array con los ids de los nodos vecinos al usuario
     * @param netData Los datos de configuración
     */
    public NetUser(UUID id, String name, InetAddress ip, UUID[] neighborhood, NetData netData)
    {
    	Log.d(nameTagLog, "entro =D" );
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.neighborhoodIds = neighborhood;
        this.signalQuality = NetUserQuality.NORMAL;
        setTimeOut(netData.getQualityMaxNetUser(), netData);
        this.jumpsAway = 0;
        this.waitTimeOut = 0;
        this.state = CommunicationQuality.NORMAL;
        this.upLayerData = new byte[0];
    }
    
    /**
     * Chequea que el usuario ha sido detectado, incrementando su valor de calidad
     * Método para uso interno
     * @param netData Los parámetros de configuración de red
     */
    public void qualityUp(NetData netData)
    {
    	Log.d(nameTagLog, "qualityUp" );
        setTimeOut(this.timeout + netData.getQualityRiseNetUser(), netData);
    }

    /**
     * Decrementa la calidad de señal, método para uso interno
     */
    public void qualityDown(NetData netData)
    {
    	Log.d(nameTagLog, "qualityDown" );
        setTimeOut(this.timeout - 1, netData);
    }
    
    /**
     * Setea el time Out del este usuario para medir calidad de señal
     * @param newTimeOut El nuevo timeout
     * @param netData Los datos de configuración de red
     */
    private void setTimeOut(int newTimeOut, NetData netData) 
    {
    	Log.d(nameTagLog, "setTimeOut" );
        if (newTimeOut > netData.getQualityMaxNetUser())
        {
            this.timeout = netData.getQualityMaxNetUser();
        }
        else
        {
            this.timeout = newTimeOut;
        }

        if (this.timeout > netData.getQualityNormalNetUser())
        {
            this.signalQuality = NetUserQuality.NORMAL;
        }
        else if (this.timeout > netData.getQualityLowNetUser())
        {
            this.signalQuality = NetUserQuality.LOW;
        }
        else
        {
            this.signalQuality = NetUserQuality.CRITICAL;
        }
    }
	
    /**
     * Selecciona un nuevo ID, método para uso interno
     */
    public void pickNewId()
    {
        this.id = UUID.randomUUID();
    }

    /**
     * Fabrica una representación del usuario como String
     * @return El nombre del usuario y su numero IP
     */
    @Override
    public String toString()
    {
        return this.name + " ("+this.ip.getHostAddress()+")";
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public UUID[] getNeighborhoodIds() {
		return neighborhoodIds;
	}

	public void setNeighborhoodIds(UUID[] neighborhoodIds) {
		this.neighborhoodIds = neighborhoodIds;
	}
	
	public void setNeighborhoodId(int pos, UUID neighborhoodId){
		this.neighborhoodIds[pos] = neighborhoodId;
	}

	public int getSignalQuality() {
		return signalQuality;
	}

	public void setSignalQuality(int signalQuality) {
		this.signalQuality = signalQuality;
	}

	/**
	 * @return Valor de señal restante
	 */
	public int getTimeout() {
		return timeout;
	}

//	public void setTimeout(int timeout) {
//		this.timeout = timeout;
//	}

	public int getJumpsAway() {
		return jumpsAway;
	}

	public void setJumpsAway(int jumpsAway) {
		this.jumpsAway = jumpsAway;
	}

	/**
	 * @return Tiempo de espera restante para intentar una conexion TCP con este usuario remoto
	 */
	public int getWaitTimeOut() {
		return waitTimeOut;
	}

	public void setWaitTimeOut(int waitTimeOut) {
		this.waitTimeOut = waitTimeOut;
	}
	
	public void waitTimeOutDown(){
		this.waitTimeOut--;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public byte[] getUpLayerData() {
		return upLayerData;
	}

	public void setUpLayerData(byte[] upLayerData) {
		this.upLayerData = upLayerData;
	}

}