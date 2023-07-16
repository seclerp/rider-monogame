using System;

namespace Rider.Plugins.MonoGame.Extensions;

public static class FlowExtensions
{
    public static T Apply<T>(this T self, Action<T> action)
    {
        action(self);
        return self;
    }

    public static TTarget Let<TSource, TTarget>(this TSource self, Func<TSource, TTarget> mapping)
    {
        return mapping(self);
    }
}