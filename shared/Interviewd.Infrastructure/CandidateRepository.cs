﻿using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using Dapper;
using Interviewd.Configuration;
using Interviewd.Domain.Model;
using Interviewd.Infrastructure.Abstraction;
using Microsoft.Extensions.Options;

namespace Interviewd.Infrastructure
{
    public class CandidateRepository : ICandidateRepository
    {
        private readonly AppSettings _AppSettings;

        private readonly IMapper _Mapper;
        
        public CandidateRepository(
            IOptions<AppSettings> appSettings,
            IMapper mapper)
        {
            _AppSettings = appSettings.Value;
            _Mapper = mapper;
        }

        public async Task<Candidate> InsertCandidate(Candidate candidate)
        {
            using (var connection = new SqlConnection(_AppSettings.ConnectionStrings.DefaultConnection))
            {
                var parameters = new DynamicParameters(
                    new
                    {
                        GivenName = candidate.GivenName,
                        Surname = candidate.Surname
                    });

                parameters.Add("Id", dbType: DbType.Int32, direction: ParameterDirection.Output);

                await connection.ExecuteAsync(
                    StoredProcedures.InsertCandidate,
                    parameters,
                    commandType: CommandType.StoredProcedure);

                candidate.Id = parameters.Get<int>("Id").ToString();

                return candidate;
            }
        }

        public async Task<Candidate> UpdateCandidate(Candidate candidate)
        {
            using (var connection = new SqlConnection(_AppSettings.ConnectionStrings.DefaultConnection))
            {
                var candidateSqlModel = _Mapper.Map<CandidateSqlModel>(candidate);

                await connection.ExecuteAsync(
                    StoredProcedures.UpdateCandidate,
                    candidateSqlModel,
                    commandType: CommandType.StoredProcedure);

                return candidate;
            }
        }

        public async Task<Candidate> GetCandidate(string id)
        {
            using (var connection = new SqlConnection(_AppSettings.ConnectionStrings.DefaultConnection))
            {
                var candidateSqlModel = (await connection.QueryAsync<CandidateSqlModel>(
                    StoredProcedures.GetCandidate,
                    new
                    {
                        Id = id
                    },
                    commandType: CommandType.StoredProcedure))
                    .Single();

                return _Mapper.Map<Candidate>(candidateSqlModel);
            }
        }

        public async Task<IEnumerable<Candidate>> GetAllCandidates()
        {
            using (var connection = new SqlConnection(_AppSettings.ConnectionStrings.DefaultConnection))
            {
                var candidateSqlModels = await connection.QueryAsync<CandidateSqlModel>(
                    StoredProcedures.GetCandidates);

                return _Mapper.Map<IEnumerable<Candidate>>(candidateSqlModels);
            }
        }
    }
}