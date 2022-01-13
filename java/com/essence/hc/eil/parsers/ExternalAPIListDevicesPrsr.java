package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonProperty;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.DeviceAssociation;
import com.essence.hc.model.ExternalAPIDevice;
import com.essence.hc.model.ExternalAPIInstallationDevices;

public class ExternalAPIListDevicesPrsr implements IParser<ExternalAPIInstallationDevices> {

	protected final Logger logger = (Logger) LogManager.getLogger(ExternalAPIListDevicesPrsr.class);
	// private Logger logger = LoggerFactory.getLogger(getClass());

	List<ExternalAPIDevicePrsr> devices;

	public ExternalAPIListDevicesPrsr() {
	}

	@Override
	public ExternalAPIInstallationDevices parse() {
		// logger.info("\nParsing " + getClass() + "...\n");
		List<ExternalAPIDevice> listDevices = new ArrayList<ExternalAPIDevice>();
		List<ExternalAPIDevice> devicesWithCorrelated = new ArrayList<ExternalAPIDevice>();
		List<ExternalAPIDevice> devicesWithAssociations = new ArrayList<ExternalAPIDevice>();
		try {
			if (devices != null)
				for (ExternalAPIDevicePrsr d : devices) {
					ExternalAPIDevice dev = (ExternalAPIDevice) d.parse();
					listDevices.add(dev);
					if ((dev.getCorrelatedDeviceIdentifier() != null)
							&& (dev.getCorrelatedDeviceIdentifier().length() > 0)) {
						devicesWithCorrelated.add(dev);
					}
					if ((dev.getDeviceAssociations() != null)
							&& (!dev.getDeviceAssociations().isEmpty())) {
						devicesWithAssociations.add(dev);
					}
				}
		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}
		// set the correlated devices
		for (ExternalAPIDevice dev : devicesWithCorrelated) {
			for (ExternalAPIDevice d : listDevices) {
				if (d.getDeviceIdentifier().compareTo(dev.getCorrelatedDeviceIdentifier()) == 0) {
					dev.setCorrelatedDev(d);
					break;
				}
			}
		}
		// set the devices with associations
		for (ExternalAPIDevice dev : devicesWithAssociations) {
			for (DeviceAssociation devAssociation : dev.getDeviceAssociations()) {
				for (ExternalAPIDevice d : listDevices) {
					if (d.getDeviceIdentifier().compareTo(devAssociation.getValue()) == 0) {
						devAssociation.setAssociatedDevice(d);
						break;
					}
				}
			}
		}

		ExternalAPIInstallationDevices iDevices = new ExternalAPIInstallationDevices();
		iDevices.setDevicesList(listDevices);

		return iDevices;
	}

	@JsonProperty("devices")
	public List<ExternalAPIDevicePrsr> getDeviceList() {
		return devices;
	}

	@JsonProperty("devices")
	public void setDeviceList(List<ExternalAPIDevicePrsr> devices) {
		this.devices = devices;
	}

}
