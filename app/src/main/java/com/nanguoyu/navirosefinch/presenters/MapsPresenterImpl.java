package com.nanguoyu.navirosefinch.presenters;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.nanguoyu.navirosefinch.MapsApplication;
import com.nanguoyu.navirosefinch.beans.GeoFenceInfo;
import com.nanguoyu.navirosefinch.module.GeoFenceManager;
import com.nanguoyu.navirosefinch.presenters.iviews.IGaoDeMapsView;

import com.nanguoyu.navirosefinch.beans.BJCamera;

import java.util.ArrayList;

/**
 * Created by andforce on 15/7/19.
 */
public class MapsPresenterImpl implements MapsPresenter, MarkerInteractor.OnMarkerCreatedListener, MarkerInteractor.OnReadCamerasListener,
        MapsActionInteractor.OnMyLocationModeChangedListener{

    private static final boolean DEBUG = false;

    private IGaoDeMapsView mGaodeMapsView;
    private MarkerInteractor mMapsInteractor;

    private MapsActionInteractor mMapsActionInteractor;

    private GeoFenceManager geoFenceManager;

    private ArrayList<BJCamera> mBeijingCameras;


    public MapsPresenterImpl(IGaoDeMapsView gaoDeMapsView) {
        this.mGaodeMapsView = gaoDeMapsView;
        this.mMapsInteractor = new MarkerInteractorImpl();
        this.mMapsActionInteractor = new MapsActionInteractorImpl();

        initCameras();
    }

    private void initCameras(){
        mMapsInteractor.readCameras(this);
    }

    @Override
    public void loadDefaultCameraMarkers() {
        mMapsInteractor.createMarkers(this);
    }

    @Override
    public void enableDefaultGeoFences() {
        if (mBeijingCameras != null){
            ArrayList<GeoFenceInfo> geoFenceInfos = new ArrayList<>();
            for (BJCamera cameraBean : mBeijingCameras){
                LatLng latLng = new LatLng(cameraBean.getLatitude(), cameraBean.getLongtitude());
                GeoFenceInfo info = new GeoFenceInfo(MapsApplication.getAppContext(),latLng,cameraBean.getId());
                geoFenceInfos.add(info);
            }

            if (DEBUG){
                GeoFenceInfo geoFenceInfo = new GeoFenceInfo(MapsApplication.getAppContext(), new LatLng(40.09705f, 116.426019f), 100);
                geoFenceInfos.add(geoFenceInfo);
            }

            geoFenceManager.addAllGeoFenceAler(geoFenceInfos);
        }
    }

    @Override
    public void disableDefaultGeoFences() {
        if (geoFenceManager != null){
            geoFenceManager.removeAllGeoFenceAlert();
        }
    }

    @Override
    public void changeMyLocationMode() {
        mMapsActionInteractor.changeMyLocationMode(this);
    }

    @Override
    public void stopFollowMode() {
        mMapsActionInteractor.stopFollowMode(this);
    }


    @Override
    public void onMarkerCreated(ArrayList<MarkerOptions> markerOptions) {
        mGaodeMapsView.addMarkers(markerOptions);
    }

    @Override
    public void onReadCameras(ArrayList<BJCamera> cameraBeans) {
        mBeijingCameras = cameraBeans;

        if (geoFenceManager == null){
            geoFenceManager = new GeoFenceManager(MapsApplication.getAppContext());
        }

        geoFenceManager.requestLocation();
    }

    @Override
    public void onMyLocationModeChanged(int mode) {
        mGaodeMapsView.changeMyLocationMode(mode);
    }

    @Override
    public void onStopFollowMode() {
        mGaodeMapsView.stopFollowMode();
    }
}
