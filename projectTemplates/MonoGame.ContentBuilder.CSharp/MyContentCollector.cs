using MonoGame.Framework.Content.Pipeline.Builder;

namespace MGNamespace;

public class MyContentCollector : ContentBuilder
{
    public override IContentCollection GetContentCollection()
    {
        var contentCollection = new ContentCollection();

        // include everything in the folder
        contentCollection.Include<RegexRule>(".");

        // override .txt files to be copied
        contentCollection.IncludeCopy<RegexRule>(".txt");

        // exclude bin / obj paths
        contentCollection.Exclude<RegexRule>("bin/");
        contentCollection.Exclude<RegexRule>("obj/");
        contentCollection.Exclude<WildcardRule>("*.mgcb");
        contentCollection.Exclude<WildcardRule>("*.contentproj");
        contentCollection.Exclude<WildcardRule>("*.xnb");
        contentCollection.Exclude<WildcardRule>("*.mgcontent");


        return contentCollection;
    }
}