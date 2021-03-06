/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.model.crypto.signature;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.xdi.oxauth.model.crypto.PrivateKey;
import org.xdi.oxauth.model.util.Base64Util;
import org.xdi.oxauth.model.util.StringUtils;

import java.math.BigInteger;

import static org.xdi.oxauth.model.jwk.JWKParameter.*;

/**
 * The Private Key for the Elliptic Curve Digital Signature Algorithm (ECDSA)
 *
 * @author Javier Rojas Blum
 * @version July 31, 2016
 */
public class ECDSAPrivateKey extends PrivateKey {

    private BigInteger d;

    public ECDSAPrivateKey(BigInteger d) {
        this.d = d;
    }

    public ECDSAPrivateKey(String d) {
        this.d = new BigInteger(1, Base64Util.base64urldecode(d));
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MODULUS, JSONObject.NULL);
        jsonObject.put(EXPONENT, JSONObject.NULL);
        jsonObject.put(D, Base64Util.base64urlencodeUnsignedBigInt(d));

        return jsonObject;
    }

    @Override
    public String toString() {
        try {
            return toJSONObject().toString(4);
        } catch (JSONException e) {
            return StringUtils.EMPTY_STRING;
        } catch (Exception e) {
            return StringUtils.EMPTY_STRING;
        }
    }
}