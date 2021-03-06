﻿using Interviewd.Common;

namespace Interviewd.Domain.Model
{
    public class Question : IIdentifiable
    {
        public string Id { get; set; }

        public string Name { get; set; }

        public string Description { get; set; }
    }
}