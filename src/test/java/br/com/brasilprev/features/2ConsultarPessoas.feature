Feature: Consultar uma pessoa no serviço
  Scenario: Efetuar a consulta de uma pessoa no serviço
    Given serviço esteja no ar
    When consultar uma pessoa "61" , "981007641"
    Then validar campo no body da consulta "Jeremias Santos da Silva"

  Scenario: Consultar uma pessoa no serviço com telefone inexistente
    Given serviço esteja no ar
    When consultar uma pessoa "61" , "989898989"
    Then verificar status code e mensagens de resposta 404, "erro"
