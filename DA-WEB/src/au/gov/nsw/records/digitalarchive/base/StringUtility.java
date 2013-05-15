package au.gov.nsw.records.digitalarchive.base;

import java.util.StringTokenizer;

public class StringUtility {

	public String[] splitString(String input)
	{
		String bufferArray[];
		StringTokenizer st = new StringTokenizer(input, "/");

		bufferArray = new String[st.countTokens()];

		int i = 0;
        while(st.hasMoreTokens())
        {
        	bufferArray[i] = st.nextToken();
            i++;
        }
        return bufferArray;
	}
	
}
