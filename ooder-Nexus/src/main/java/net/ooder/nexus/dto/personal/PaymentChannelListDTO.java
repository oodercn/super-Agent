package net.ooder.nexus.dto.personal;

import net.ooder.nexus.domain.personal.model.PaymentChannel;

import java.io.Serializable;
import java.util.List;

public class PaymentChannelListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<PaymentChannel> channels;

    public List<PaymentChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<PaymentChannel> channels) {
        this.channels = channels;
    }
}
