package lat.trust.trusttrifles.utilities;

import lat.trust.trusttrifles.network.RestClientCompany;
import lat.trust.trusttrifles.network.req.SaveDeviceInfoRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveDeviceInfo {

    public static void saveDeviceInfo(final String dni, final String bundleId, final String trustId) {
        TrustLogger.d("[TRUST CLIENT] SAvING DEVICE...");
        SaveDeviceInfoRequest saveDeviceInfo = new SaveDeviceInfoRequest();
        saveDeviceInfo.setBundle_id(bundleId);
        saveDeviceInfo.setDni(dni);
        saveDeviceInfo.setTrust_id(trustId);
        RestClientCompany.setup().saveDeviceData(saveDeviceInfo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    TrustLogger.d("[TRUST CLIENT] DEVICE WAS SAVED CODE: " + response.code() + "DNI: " + dni + " BUNDLE ID: " + bundleId + " TRUST ID: " + trustId);
                } else {
                    TrustLogger.d("[TRUST CLIENT] SAvING DEVICE CODE: " + response.code());

                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                TrustLogger.d("[TRUST CLIENT] SAvING DEVICE ERROR: " + t.getMessage());
            }
        });
    }
}
