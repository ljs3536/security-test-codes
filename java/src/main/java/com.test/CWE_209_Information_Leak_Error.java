import testcasesupport.*;

public class CWE_209_Information_Leak_Error extends AbstractTestCase
{
    public void bad() throws Throwable
    {

        try
        {
            throw new UnsupportedOperationException();
        }
        catch (UnsupportedOperationException exceptUnsupportedOperation)
        {
            exceptUnsupportedOperation.printStackTrace(); /* FLAW: Print stack trace to console on error */
        }

    }

    public void good() throws Throwable
    {
        good1();
    }

    private void good1() throws Throwable
    {

        try
        {
            throw new UnsupportedOperationException();
        }
        catch (UnsupportedOperationException exceptUnsupportedOperation)
        {
            IO.writeLine("There was an unsupported operation error"); /* FIX: print a generic message */
        }

    }
}

