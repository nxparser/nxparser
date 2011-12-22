package org.semanticweb.yars.nx.filter;

import java.util.regex.Pattern;

import org.semanticweb.yars.nx.Node;

public interface NodeFilter {
	public boolean check(Node n);
	
	public static abstract class AbstractFilter implements NodeFilter{
		boolean _negate;
		
		protected AbstractFilter(boolean negate){
			_negate = negate;
		}
		
		protected AbstractFilter(){
			_negate = false;
		}
		
		public final boolean check(Node n){
			return _negate ^ matches(n);
		}
		
		protected abstract boolean matches(Node n);
	}
	
	public static class EqualsFilter extends AbstractFilter{
		String _equals;
		
		public EqualsFilter(String n, boolean negate){
			super(negate);
			_equals = n;
		}
		
		public EqualsFilter(String n){
			this(n, false);
		}
		
		protected boolean matches(Node n){
			return _equals.equals(n.toString());
			
		}
	}
	
	public static class PrefixFilter extends AbstractFilter{
		String _namespace;
		
		public PrefixFilter(String ns){
			this(ns, false);
		}
		
		public PrefixFilter(String ns, boolean negate){
			super(negate);
			_namespace = ns;
		}

		protected boolean matches(Node n) {
			return n.toString().startsWith(_namespace);
		}
	}
	
	public static class RegexFilter extends AbstractFilter{
		Pattern _pattern;
		
		public static final char CASE_INSENSITIVE = 'i';
		public static final char DOT_ALL = 's';
		public static final char MULTI_LINE = 'm';
		public static final char REMOVE_WHITESPACE = 'x';
		
		public RegexFilter(String pattern, int flagMask){
			this(pattern, flagMask, false);
		}
		
		public RegexFilter(String pattern, int flagMask, boolean negate){
			super(negate);
			_pattern = Pattern.compile(pattern, flagMask);
		}
		
		public RegexFilter(String pattern){
			this(pattern, false);
		}
		
		public RegexFilter(String pattern, boolean negate){
			super(negate);
			_pattern = Pattern.compile(pattern);
		}
		
		protected boolean matches(Node n){
			return _pattern.matcher(n.toString()).matches();
		}
		
		public RegexFilter(String pattern, String flag){
			this(pattern, flag, false);
		}
		
		public RegexFilter(String pattern, String flag, boolean negate){
			super(negate);
			if(flag!=null && flag.trim().length()>0){
				flag = flag.trim();
				int[] flags = new int[flag.length()];
				for(int i=0; i<flag.length(); i++){
					char c = flag.charAt(i);
					if(c == CASE_INSENSITIVE){
						flags[i] = Pattern.CASE_INSENSITIVE;
					} else if(c == DOT_ALL){
						flags[i] = Pattern.DOTALL;
					} else if(c == MULTI_LINE){
						flags[i] = Pattern.MULTILINE;
					} else if(c == REMOVE_WHITESPACE){
						flags[i] = Pattern.COMMENTS;
					} else throw new IllegalArgumentException("Cannot recognise REGEX flag :"+flag+"." +
							"Must be "+CASE_INSENSITIVE+" "+DOT_ALL+" "+MULTI_LINE+" or "+REMOVE_WHITESPACE);
				}
				
				int bm = 0;
				if(flags==null || flags.length==0)
					;
				else if(flags.length==1)
					bm = flags[0];
				else{
					bm = flags[0];
					for(int i=1; i<flags.length; i++){
						bm |= flags[i];
					}
				}

				_pattern = Pattern.compile(pattern, bm);
			} else _pattern = Pattern.compile(pattern);
		}
	}
	
	public static class ClassFilter extends AbstractFilter{
		Class<? extends Node> _class;
		
		public ClassFilter(Class<? extends Node> c){
			this(c, false);
		}
		
		public ClassFilter(Class<? extends Node> c, boolean negate){
			super(negate);
			_class = c;
		}
		
		protected boolean matches(Node n){
			return _class.isInstance(n);
		}
	}
	
	public static class AndFilter implements NodeFilter{
		NodeFilter[] _filters;
		
		public AndFilter(NodeFilter... filters){
			_filters = filters;
		}
		
		public boolean check(Node n){
			for(NodeFilter filter:_filters){
				if(!filter.check(n))
					return false;
			}
			return true;
		}
	}
	
	public static class OrFilter implements NodeFilter{
		NodeFilter[] _filters;
		
		public OrFilter(NodeFilter... filters){
			_filters = filters;
		}
		
		public boolean check(Node n){
			for(NodeFilter filter:_filters){
				if(filter.check(n))
					return true;
			}
			return false;
		}
	}
}
