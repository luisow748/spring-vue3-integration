package com.luisow748.springvue3.inertia;


import org.springframework.web.servlet.ModelAndView;

public class InertiaVue {

    private static final String INERTIA_VIEW_NAME = "inertiaView";

    public static ModelAndView render(String component) {
        return new ModelAndView(INERTIA_VIEW_NAME)
            .addObject("component", component);
    }

    public static ModelAndView render(String component, Props props) {
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
