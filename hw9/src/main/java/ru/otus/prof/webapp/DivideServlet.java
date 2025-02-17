package ru.otus.prof.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/divide")
public class DivideServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double a = Double.parseDouble(req.getParameter("a"));
        double b = Double.parseDouble(req.getParameter("b"));
        if (b == 0) {
            resp.getWriter().write("Error: Division by zero");
        } else {
            double result = a / b;
            resp.getWriter().write(String.format("%s / %s = %s", a, b, result));
        }

    }
}
