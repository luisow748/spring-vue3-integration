package com.luisow748.springvue3.inertia;

public class InertiaVuePage {
    String component;
    Object props;
    String url;

    public static InertiaVuePage get(String component, String props, String url) {
        InertiaVuePage page = new InertiaVuePage();
        page.url = url;
        page.component = component;
        page.props = props;
        return page;
    }
}
