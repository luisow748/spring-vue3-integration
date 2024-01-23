package inertia.spring.inertia;


import org.springframework.web.servlet.ModelAndView;

public class Inertia {

    private static final String INERTIA_VIEW_NAME = "inertiaView";

    public static ModelAndView renderVueComponent(String component) {
        return new ModelAndView(INERTIA_VIEW_NAME)
            .addObject("component", component);
    }

    public static ModelAndView renderVueComponent(String component, Props props) {
        return new ModelAndView(INERTIA_VIEW_NAME)
            .addObject("component", component)
            .addObject("props", props.getContent());
    }

    public static class Props {
        private final Object content;

        private Props(Object viewModel) {
            this.content = viewModel;
        }

        public static Props withProps(Object viewModel) {
            return new Props(viewModel);
        }

        public Object getContent() {
            return content;
        }
    }
}
