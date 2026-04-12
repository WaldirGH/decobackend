package com.deco.apideco.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

@Service
public class MercadoPagoService {

        @Value("${MP_ACCESS_TOKEN}")
        private String accessToken;

        public String crearPreferencia(Double monto, String descripcion) throws Exception {
                MercadoPagoConfig.setAccessToken(accessToken);

                PreferenceItemRequest item = PreferenceItemRequest.builder()
                                .title(descripcion)
                                .quantity(1)
                                .unitPrice(BigDecimal.valueOf(monto))
                                .currencyId("PEN")
                                .build();

                PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                .success("https://papaya-mooncake-52ac6c.netlify.app/cart?pago=ok")
                                .failure("https://papaya-mooncake-52ac6c.netlify.app/cart?pago=error")
                                .pending("https://papaya-mooncake-52ac6c.netlify.app/cart?pago=pendiente")
                                .build();

                PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                .items(List.of(item))
                                .backUrls(backUrls)
                                .autoReturn("approved")
                                .build();

                PreferenceClient client = new PreferenceClient();
                Preference preference = client.create(preferenceRequest);

                return preference.getInitPoint();
        }
}