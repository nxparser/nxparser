package scratch;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.semanticweb.yars.nx.util.NxUtil;

public class blah {
	public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException{
		String s = "http://ru.dbpedia.org/resource/Эдж_(остров)";
		
		
		String a = URLEncoder.encode("Эдж", "utf8");
		String b = URLEncoder.encode("остров", "utf8");
		
		System.err.println(NxUtil.escapeForNx(s));
		System.err.println("http://ru.dbpedia.org/resource/"+a+"_("+b+")");
	}
}
