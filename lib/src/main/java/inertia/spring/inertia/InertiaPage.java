package inertia.spring.inertia;

public class InertiaPage {
    String component;
    Object props;
    String url;

    public static InertiaPage get(String component, String props, String url) {
        InertiaPage page = new InertiaPage();
        page.url = url;
        page.component = component;
        page.props = props;
        return page;
    }
}
