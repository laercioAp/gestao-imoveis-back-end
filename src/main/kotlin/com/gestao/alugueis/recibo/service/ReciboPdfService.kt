
package com.gestao.alugueis.recibo.service

import com.gestao.alugueis.recibo.entity.Recibo
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.springframework.stereotype.Service
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class ReciboPdfService {

    fun gerar(recibo: Recibo): ByteArray {

        val pagamento = recibo.pagamento
        val contrato = pagamento.contrato
        val imovel = contrato.imovel
        val inquilino = contrato.inquilino

        val documento = PDDocument()
        val pagina = PDPage(PDRectangle.A4)
        documento.addPage(pagina)

        val saida = ByteArrayOutputStream()

        val fonte = PDType1Font(Standard14Fonts.FontName.HELVETICA)
        val fonteNegrito = PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)

        val azulMarinho = Color(20, 43, 77)
        val azul = Color(36, 103, 170)
        val azulClaro = Color(232, 241, 250)
        val cinza = Color(95, 105, 120)
        val cinzaClaro = Color(220, 228, 237)
        val branco = Color.WHITE
        val preto = Color(35, 43, 54)

        val moeda = NumberFormat.getCurrencyInstance(
            Locale.forLanguageTag("pt-BR")
        )

        val formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatoCompetencia = DateTimeFormatter.ofPattern("yyyy-MM")

        // Número curto para apresentação no PDF.
        // O número original do recibo continua preservado no banco.
        val numeroReciboExibicao =
            "REC-${recibo.id?.toString()?.padStart(4, '0') ?: "0000"}"

        PDPageContentStream(documento, pagina).use { conteudo ->

            fun normalizar(texto: String): String {
                return texto
                    .replace("\u00A0", " ")
                    .replace("–", "-")
                    .replace("—", "-")
            }

            fun texto(
                valor: String,
                x: Float,
                y: Float,
                tamanho: Float = 10f,
                negrito: Boolean = false,
                cor: Color = preto,
                larguraMaxima: Float = 480f
            ) {
                val fonteAtual = if (negrito) fonteNegrito else fonte
                var textoFinal = normalizar(valor)

                while (
                    textoFinal.isNotEmpty() &&
                    fonteAtual.getStringWidth(textoFinal) / 1000f * tamanho > larguraMaxima
                ) {
                    textoFinal = textoFinal.dropLast(1)
                }

                conteudo.beginText()
                conteudo.setFont(fonteAtual, tamanho)
                conteudo.setNonStrokingColor(cor)
                conteudo.newLineAtOffset(x, y)
                conteudo.showText(textoFinal)
                conteudo.endText()
            }

            // Escreve um texto centralizado em relação ao ponto informado.
            fun textoCentralizado(
                valor: String,
                centroX: Float,
                y: Float,
                tamanho: Float = 10f,
                negrito: Boolean = false,
                cor: Color = preto
            ) {
                val fonteAtual = if (negrito) fonteNegrito else fonte
                val textoFinal = normalizar(valor)

                val larguraTexto =
                    fonteAtual.getStringWidth(textoFinal) / 1000f * tamanho

                val x = centroX - (larguraTexto / 2f)

                conteudo.beginText()
                conteudo.setFont(fonteAtual, tamanho)
                conteudo.setNonStrokingColor(cor)
                conteudo.newLineAtOffset(x, y)
                conteudo.showText(textoFinal)
                conteudo.endText()
            }

            fun retangulo(
                x: Float,
                y: Float,
                largura: Float,
                altura: Float,
                preenchimento: Color,
                borda: Color? = null,
                espessura: Float = 1f
            ) {
                conteudo.setNonStrokingColor(preenchimento)
                conteudo.addRect(x, y, largura, altura)
                conteudo.fill()

                if (borda != null) {
                    conteudo.setStrokingColor(borda)
                    conteudo.setLineWidth(espessura)
                    conteudo.addRect(x, y, largura, altura)
                    conteudo.stroke()
                }
            }

            fun linha(
                x1: Float,
                y1: Float,
                x2: Float,
                y2: Float,
                cor: Color = cinzaClaro
            ) {
                conteudo.setStrokingColor(cor)
                conteudo.setLineWidth(0.8f)
                conteudo.moveTo(x1, y1)
                conteudo.lineTo(x2, y2)
                conteudo.stroke()
            }

            fun secao(
                titulo: String,
                x: Float,
                y: Float,
                largura: Float
            ) {
                retangulo(x, y - 8f, 4f, 18f, azul)
                texto(titulo, x + 13f, y - 3f, 11f, true, azulMarinho, largura - 20f)
            }

            fun campo(
                rotulo: String,
                valor: String,
                x: Float,
                y: Float,
                largura: Float
            ) {
                texto(rotulo, x, y, 8f, false, cinza, largura)
                texto(valor, x, y - 15f, 10f, true, preto, largura)
            }

            val margem = 38f
            val larguraPagina = 595f
            val larguraConteudo = larguraPagina - (margem * 2)

            // Fundo e borda externa
            retangulo(
                0f, 0f, larguraPagina, 842f,
                branco
            )

            retangulo(
                margem, 35f,
                larguraConteudo, 772f,
                branco, azul, 1.5f
            )

            // Cabeçalho azul-marinho
            retangulo(
                margem, 715f,
                larguraConteudo, 92f,
                azulMarinho
            )

            // Cabeçalho centralizado
            textoCentralizado(
                "RECIBO DE ALUGUEL",
                297.5f, 770f,
                21f, true, branco
            )

            textoCentralizado(
                "COMPROVANTE DE PAGAMENTO",
                297.5f, 748f,
                10f, false, Color(205, 220, 239)
            )

            // Identificação do recibo
            texto(
                "NÚMERO DO RECIBO",
                58f, 692f,
                8f, true, cinza
            )

            texto(
                numeroReciboExibicao,
                58f, 674f,
                11f, true, azulMarinho, 180f
            )

            texto(
                "EMISSÃO",
                390f, 692f,
                8f, true, cinza
            )

            texto(
                recibo.dataEmissao?.format(formatoData) ?: "-",
                390f, 674f,
                11f, true, azulMarinho, 140f
            )

            linha(58f, 657f, 537f, 657f)

            // Dados das partes
            secao("IDENTIFICAÇÃO DAS PARTES", 58f, 635f, 470f)

            retangulo(
                58f, 558f, 479f, 59f,
                azulClaro, cinzaClaro
            )

            campo(
                "LOCADOR",
                "Laercio Aparecido Pedroso",
                72f, 606f, 445f
            )

            campo(
                "INQUILINO",
                inquilino.nome,
                72f, 578f, 445f
            )

            // Dados do imóvel
            secao("DADOS DO IMÓVEL", 58f, 531f, 470f)

            retangulo(
                58f, 449f, 479f, 64f,
                branco, cinzaClaro
            )

            campo(
                "IMÓVEL",
                imovel.titulo,
                72f, 501f, 445f
            )

            campo(
                "ENDEREÇO",
                "${imovel.endereco}, ${imovel.cidade} - ${imovel.estado}",
                72f, 471f, 445f
            )

            // Informações do pagamento
            secao("INFORMAÇÕES DO PAGAMENTO", 58f, 423f, 470f)

            retangulo(
                58f, 330f, 479f, 75f,
                branco, cinzaClaro
            )

            campo(
                "COMPETÊNCIA",
                pagamento.competencia.format(formatoCompetencia),
                72f, 384f, 135f
            )

            campo(
                "VENCIMENTO",
                pagamento.dataVencimento.format(formatoData),
                230f, 384f, 135f
            )

            campo(
                "DATA DO PAGAMENTO",
                pagamento.dataPagamento?.format(formatoData) ?: "-",
                390f, 384f, 135f
            )

            campo(
                "FORMA DE PAGAMENTO",
                pagamento.formaPagamento ?: "-",
                72f, 348f, 200f
            )

            // Valor recebido em destaque
            retangulo(
                58f, 245f, 479f, 67f,
                azulClaro, azul, 1.3f
            )

            texto(
                "VALOR RECEBIDO",
                76f, 287f,
                10f, true, azulMarinho
            )

            texto(
                moeda.format(pagamento.valorPago ?: pagamento.valorPrevisto),
                76f, 262f,
                22f, true, azulMarinho, 440f
            )

            // Declaração de recebimento
            texto(
                "DECLARAÇÃO DE RECEBIMENTO",
                58f, 215f,
                9f, true, azulMarinho
            )

            texto(
                "Declaro o recebimento do valor acima referente à locação informada.",
                58f, 193f,
                10f, false, preto, 470f
            )

            texto(
                "Este documento comprova o recebimento do pagamento identificado.",
                58f, 176f,
                9f, false, cinza, 470f
            )

            // Identificação do locador, sem linha de assinatura
            texto(
                "Laercio Aparecido Pedroso",
                58f, 108f,
                10f, true, azulMarinho
            )

            texto(
                "Locador",
                58f, 92f,
                9f, false, cinza
            )

            // Rodapé
            linha(58f, 73f, 537f, 73f)

            textoCentralizado(
                "Recibo gerado pelo Sistema de Gestão de Aluguéis.",
                297.5f, 54f,
                8f, false, cinza
            )
        }

        documento.save(saida)
        documento.close()

        return saida.toByteArray()
    }
}