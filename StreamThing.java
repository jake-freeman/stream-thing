public class StreamThing
{
    public static void main (String[] args)
    {
        try
        {
            StreamThing obj = new StreamThing();
            obj.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void run()
    {
        new STFrame();
    }
}
