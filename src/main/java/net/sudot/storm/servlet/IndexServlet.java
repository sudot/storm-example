package net.sudot.storm.servlet;

import net.sudot.storm.TopologyMain;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tangjialin on 2017-03-29 0029.
 */
@WebServlet(urlPatterns = {"/servlet"})
public class IndexServlet extends HttpServlet {
    Jedis jedis = new Jedis(TopologyMain.REDIS_HOST, TopologyMain.REDIS_PORT);

    @Override
    public void init(ServletConfig config) throws ServletException {
        jedis.del(TopologyMain.CLICK_KEY);
        jedis.del(TopologyMain.RESULT_KEY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("click".equals(action)) {
            System.out.println("产生点击");
            jedis.lpush(TopologyMain.CLICK_KEY, req.getSession().getId());
        } else if ("count".equals(action)) {
            String result = jedis.get(TopologyMain.RESULT_KEY);
            result = result == null || "nul".equals(result) ? "0" : result;
            resp.getOutputStream().write(result.getBytes("UTF-8"));
            resp.getOutputStream().close();
        } else if ("clean".equals(action)) {
            init(null);
        }
    }
}
