package com.essence.hc.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.essence.hc.exceptions.AppRuntimeException;
import com.essence.hc.model.Activity;
import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Device;
import com.essence.hc.model.ExternalAPIDevice;
import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.InstallationDevices;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.persistence.InstallationDAO;
import com.essence.hc.persistence.UserDAO;
import com.essence.hc.service.InstallationService;

/**
 * @author daniel.alcantarilla
 *
 */
@Service
public class InstallationServiceImpl implements InstallationService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final Map<ServiceType, Set<ActivityType>> installableDevices = new HashMap<>();
	static {
		Set<ActivityType> activityTypesAnalytics = new TreeSet<>(Arrays.asList(ActivityType.values()));
		// Analytics
		installableDevices.put(ServiceType.ANALYTICS, activityTypesAnalytics);

		// Express
		Set<ActivityType> activityTypesExpress = new TreeSet<>(Arrays.asList(ActivityType.values()));
		activityTypesExpress.remove(ActivityType.FRIDGE_DOOR);
		installableDevices.put(ServiceType.EXPRESS, activityTypesExpress);

		Set<ActivityType> activityTypesPerse = new TreeSet<>();
		activityTypesPerse.add(ActivityType.EP);
		activityTypesPerse.add(ActivityType.EPPlus);
		activityTypesPerse.add(ActivityType.EPA);
		activityTypesPerse.add(ActivityType.FALL_DETECTOR);
		activityTypesPerse.add(ActivityType.VPD);
		activityTypesPerse.add(ActivityType.SPBP);
		activityTypesPerse.add(ActivityType.SMOKE_DETECTOR);
		activityTypesPerse.add(ActivityType.WATER_LEAKAGE);
		installableDevices.put(ServiceType.PERSE, activityTypesPerse);
	}

	@Autowired
	InstallationDAO installationDAO;

	@Autowired
	UserDAO userDAO;

	@Override
	@PreAuthorize("isAuthenticated()")
	public Set<ActivityType> getInstallableDevices(Patient patient) {
		return installableDevices.get(patient.getServiceType());
	}

	@PreAuthorize("isAuthenticated()")
	public boolean hasCorrelatedDevOpt(Patient patient) {
		return patient.getServiceType().equals(ServiceType.ANALYTICS);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus DeviceAddByUserId(int userId, int deviceType, int activityTypeId, int deviceId, String label,
			boolean isIPD, int relatedDevID) { // , int ErrorMessageId)
		return installationDAO.DeviceAddByUserId(userId, deviceType, activityTypeId, deviceId, label, isIPD,
				relatedDevID); // , ErrorMessageId);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus DeviceUpdateByUserId(int userId, int deviceType, int activityTypeId, int deviceId,
			String label, boolean isIPD, int ErrorMessageId, int OriginalDeviceId, int oldDeviceType,
			int relatedDevID) {
		return installationDAO.DeviceUpdateByUserId(userId, deviceType, activityTypeId, deviceId, label, isIPD,
				ErrorMessageId, OriginalDeviceId, oldDeviceType, relatedDevID);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus ExternalAPIDeviceUpdateByUserId(int userId, int deviceType, int deviceFamily, int deviceId,
			int activityTypeId, String label, boolean isIPD, int ErrorMessageId, String deviceIdentifier,
			int oldDeviceType, String relatedDevIdentifier) {
		return installationDAO.ExternalAPIDeviceUpdateByUserId(userId, deviceType, deviceFamily, deviceId,
				activityTypeId, label, isIPD, ErrorMessageId, deviceIdentifier, oldDeviceType, relatedDevIdentifier);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseStatus DeviceDeleteByUserId(int userId, int deviceType, int deviceId) {
		return installationDAO.DeviceDeleteByUserId(userId, deviceType, deviceId);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public InstallationDevices DeviceListByUserId(String userId) {
		InstallationDevices iDevices = installationDAO.DeviceListByUserId(userId);
		iDevices.setDevicesList(sortDevicesByFamily(iDevices.getDevicesList()));
		return iDevices;
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public ExternalAPIInstallationDevices DeviceListByExternalAPI(String account, String userId) {
		ExternalAPIInstallationDevices iDevices = installationDAO.DeviceListByExternalAPI(account);
		// 2.5.5 Hack: To get synchronized field, we use "supportActiveService" property
		// of User
		try {
			Patient iPatient = (Patient) userDAO.getUser(userId);

			iDevices.setIsSyncronized(iPatient.getIsActiveService());
			// JAA: Sort list
			iDevices.setDevicesList(sortExternalAPIDevicesByFamily(iDevices.getDevicesList()));
			return iDevices;
		} catch (Exception e) {
			throw new AppRuntimeException("This user is not a patient. No devices attached.");
		}
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public Device GetDevice(int deviceId) {
		return installationDAO.GetDevice(deviceId);
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public List<Panel> GetFreeAccounts() {
		return installationDAO.GetFreeAccounts();
	}

	private List<Device> sortDevicesByFamily(List<Device> devices) {

		List<Device> activitySensors = new ArrayList<Device>();
		List<Device> safetyAndSos = new ArrayList<Device>();

		List<Device> sortedList = new ArrayList<Device>();

		for (int i = 0; i < devices.size(); i++) {
			Device myDevice = devices.get(i);
			int myFamilyId = myDevice.getFamilyId();

			if (myFamilyId == Activity.ActivityFamily.FAMILY_ID_ACTIVITY_SENSOR.getValue()) {
				activitySensors.add(myDevice);
			} else if (myFamilyId == Activity.ActivityFamily.FAMILY_ID_SAFETY_AND_SOS.getValue()) {
				safetyAndSos.add(myDevice);
			}
		}

		// Sort lists
		activitySensors = sortDeviceListByDevRoomId(activitySensors);
		safetyAndSos = sortDeviceListByDevRoomId(safetyAndSos);

		sortedList.addAll(activitySensors);
		sortedList.addAll(safetyAndSos);

		return sortedList;
	}

	private List<ExternalAPIDevice> sortExternalAPIDevicesByFamily(List<ExternalAPIDevice> devices) {

		List<ExternalAPIDevice> activitySensors = new ArrayList<ExternalAPIDevice>();
		List<ExternalAPIDevice> safetyAndSos = new ArrayList<ExternalAPIDevice>();

		List<ExternalAPIDevice> sortedList = new ArrayList<ExternalAPIDevice>();

		for (int i = 0; i < devices.size(); i++) {
			ExternalAPIDevice myDevice = devices.get(i);
			int myFamilyId = myDevice.getDeviceFamily();

			if (myFamilyId == Activity.ActivityFamily.FAMILY_ID_ACTIVITY_SENSOR.getValue()) {
				activitySensors.add(myDevice);
			} else if (myFamilyId == Activity.ActivityFamily.FAMILY_ID_SAFETY_AND_SOS.getValue()) {
				safetyAndSos.add(myDevice);
			}
		}

		// Sort lists
		activitySensors = sortExternalAPIDeviceListByDeviceId(activitySensors);
		safetyAndSos = sortExternalAPIDeviceListByDeviceId(safetyAndSos);

		sortedList.addAll(activitySensors);
		sortedList.addAll(safetyAndSos);

		return sortedList;
	}

	private List<Device> sortDeviceListByDevRoomId(List<Device> unorderedList) {

		// Unicity and order has to be guaranteed: EIC15-1425
		SortedSet<Device> orderedSet = new TreeSet<Device>(new DeviceDevRoomIdComparator());
		for (Device dev : unorderedList) {
			orderedSet.add(dev);
		}
		return new ArrayList<Device>(orderedSet);
	}

	private List<ExternalAPIDevice> sortExternalAPIDeviceListByDeviceId(List<ExternalAPIDevice> unorderedList) {

		// Unicity and order has to be guaranteed: EIC15-1425
		SortedSet<ExternalAPIDevice> orderedSet = new TreeSet<ExternalAPIDevice>(new ExternalAPIDeviceIdComparator());
		for (ExternalAPIDevice dev : unorderedList) {
			orderedSet.add(dev);
		}
		return new ArrayList<ExternalAPIDevice>(orderedSet);
	}

	class DeviceDevRoomIdComparator implements Comparator<Device> {

		@Override
		public int compare(Device o1, Device o2) {
			return o1.getDevRoomID() - o2.getDevRoomID();
		}

	}

	class ExternalAPIDeviceIdComparator implements Comparator<ExternalAPIDevice> {

		@Override
		public int compare(ExternalAPIDevice o1, ExternalAPIDevice o2) {
			return o1.getDeviceId() - o2.getDeviceId();
		}

	}

}
