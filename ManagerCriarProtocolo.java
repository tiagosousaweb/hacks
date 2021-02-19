package br.com.foxinline.manager.criar;

import br.com.foxinline.annotations.AccessPermission;
import br.com.foxinline.enums.AplicacaoCampo;
import br.com.foxinline.enums.CategoriaPessoa;
import br.com.foxinline.enums.CategoriaProtocolo;
import br.com.foxinline.enums.Classe;
import br.com.foxinline.enums.FormaPagamento;
import br.com.foxinline.enums.SituacaoTramite;
import br.com.foxinline.enums.StatusProtocolo;
import br.com.foxinline.enums.TipoDesconto;
import br.com.foxinline.enums.TipoFinanceiro;
import br.com.foxinline.enums.TipoGratuidade;
import br.com.foxinline.enums.TipoPessoa;
import br.com.foxinline.enums.TipoProtocolo;
import br.com.foxinline.helper.ProtocoloItemHelper;
import br.com.foxinline.manager.BasicManagerCreate;
import br.com.foxinline.manager.pesquisar.ManagerPesquisarProtocolo;
import br.com.foxinline.model.UserSystem;
import br.com.foxinline.modelo.Anexo;
import br.com.foxinline.modelo.Ato;
import br.com.foxinline.modelo.AtoCampo;
import br.com.foxinline.modelo.Autenticacao;
import br.com.foxinline.modelo.BaseLegal;
import br.com.foxinline.modelo.Cartorio;
import br.com.foxinline.modelo.ConfiguracaoSistema;
import br.com.foxinline.modelo.DutEletronico;
import br.com.foxinline.modelo.Emolumento;
import br.com.foxinline.modelo.EmolumentoItem;
import br.com.foxinline.modelo.Imposto;
import br.com.foxinline.modelo.ImpostoProtocolo;
import br.com.foxinline.modelo.ImpostoProtocoloItem;
import br.com.foxinline.modelo.ItemModeloProcesso;
import br.com.foxinline.modelo.ItemReconhecimentoFirma;
import br.com.foxinline.modelo.ModeloProcesso;
import br.com.foxinline.modelo.Parcela;
import br.com.foxinline.modelo.Pessoa;
import br.com.foxinline.modelo.Protesto;
import br.com.foxinline.modelo.Protocolo;
import br.com.foxinline.modelo.ProtocoloCampo;
import br.com.foxinline.modelo.ProtocoloItem;
import br.com.foxinline.modelo.ReconhecimentoFirma;
import br.com.foxinline.modelo.Telefone;
import br.com.foxinline.modelo.TipoLivro;
import br.com.foxinline.modelo.Tramite;
import br.com.foxinline.modelo.ep.AtoTipoEP;
import br.com.foxinline.modelo.ep.Parte;
import br.com.foxinline.modelo.ep.Qualidade;
import br.com.foxinline.servico.AnexoServico;
import br.com.foxinline.servico.AtoCampoServico;
import br.com.foxinline.servico.AtoServico;
import br.com.foxinline.servico.AutenticacaoServico;
import br.com.foxinline.servico.BaseLegalServico;
import br.com.foxinline.servico.CartorioServico;
import br.com.foxinline.servico.ConfiguracaoSistemaServico;
import br.com.foxinline.servico.DutEletronicoServico;
import br.com.foxinline.servico.EmolumentoItemServico;
import br.com.foxinline.servico.EmolumentoServico;
import br.com.foxinline.servico.ImpostoServico;
import br.com.foxinline.servico.LivroServico;
import br.com.foxinline.servico.ModeloProcessoServico;
import br.com.foxinline.servico.ParcelaServico;
import br.com.foxinline.servico.PessoaServico;
import br.com.foxinline.servico.ProtestoServico;
import br.com.foxinline.servico.ProtocoloServico;
import br.com.foxinline.servico.ReconhecimentoFirmaServico;
import br.com.foxinline.servico.SelarServico;
import br.com.foxinline.servico.TipoLivroServico;
import br.com.foxinline.servico.UsuarioServico;
import br.com.foxinline.servico.ep.QualidadeServico;
import br.com.foxinline.util.DateUtils;
import br.com.foxinline.util.MessageUtils;
import br.com.foxinline.util.UserUtils;
import br.com.foxinline.util.Utils;
import br.com.foxinline.utilitario.BaseLegalUtils;
import br.com.foxinline.utilitario.Caracteres;
import br.com.foxinline.utilitario.Mensagem;
import br.com.foxinline.utilitario.PessoaUtils;
import br.com.foxinline.utilitario.ProtocoloUtils;
import br.com.foxinline.utilitario.RoundUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.component.selectbooleanbutton.SelectBooleanButton;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author cadomeireles
 */
@ViewScoped
@ManagedBean
@AccessPermission(moduleName = "PROTOCOLO", pageRedirect = "indexProtocolo.xhtml")
public class ManagerCriarProtocolo extends BasicManagerCreate implements Serializable {

    @EJB
    ProtocoloServico protocoloServico;
    @EJB
    PessoaServico pessoaServico;
    @EJB
    AtoServico atoServico;
    @EJB
    EmolumentoItemServico emolumentoItemServico;
    @EJB
    EmolumentoServico emolumentoServico;
    @EJB
    AtoCampoServico atoCampoServico;
    @EJB
    LivroServico livroServico;
    @EJB
    TipoLivroServico tipoLivroServico;
    @EJB
    CartorioServico cartorioServico;
    @EJB
    UsuarioServico usuarioServico;
    @EJB
    ParcelaServico parcelaServico;
    @EJB
    AnexoServico anexoServico;
    @EJB
    QualidadeServico qualidadeServico;
    @EJB
    ConfiguracaoSistemaServico configuracaoSistemaServico;
    @EJB
    ProtestoServico protestoServico;
    @EJB
    AutenticacaoServico autenticacaoServico;
    @EJB
    ImpostoServico impostoServico;
    @EJB
    ModeloProcessoServico modeloProcessoServico;
    @EJB
    SelarServico selarServico;
    @EJB
    BaseLegalServico baseLegalServico;
    @EJB
    ReconhecimentoFirmaServico reconhecimentoFirmaServico;
    @EJB
    DutEletronicoServico dutEletronicoServico;
    private Protocolo protocolo;
    private List<ProtocoloItem> protocoloItens;
    private List<ProtocoloItem> protocolosItensRemovidos;
    private EmolumentoItem emolumentoItem;
    private boolean gratuidadeItem;
    private List<ProtocoloCampo> protocoloCampos;
    private Parcela parcela;
    private List<Parcela> parcelas;
    private Ato ato;
    private List<AtoCampo> atoCampos;
    private BigDecimal valorTotalProtocolo;
    private BigDecimal valorTotalProtocoloSemDesconto;
    private String textoNotaDevolucao;
    private Long quantidade;
    private Cartorio cartorio;
    private String editarId;
    public BigDecimal troco;
    private Anexo anexo;
    private UploadedFile arquivo;
    private List<Anexo> anexosRemovidos;
    public BigDecimal valorCobradoProtocoloItemAnterior;
    private boolean cobrancaIndevidaProtocoloItem;
    private SelectBooleanButton gratuitoBooleanButton;
    private boolean protocoloFirma = false;
    private Protocolo protocoloVinculado;
    private boolean editarApenasAnexo;
    private Parte parte;
    private TipoPessoa tipoPessoaParte;
    private ConfiguracaoSistema configuracaoSistema;
    private boolean controlesalva = false;
    private boolean administradorEdicaoProcesso = false;
    private boolean limitarEdicaoProcesso = false;
    private ProtocoloItem protocoloItem;
    private boolean validarAtoComClienteNoProcesso;
    private List<Imposto> impostos;
    private ModeloProcesso modeloProcesso;
    private boolean limitarQuantidadePrenotacao = false;
    private String parametroSeloFirmaAutenticacao = null;
    private boolean abreDialogCliente = false;
    private Boolean processoFirmaAutenticacao;
    private TipoGratuidade tipoGratuidadeProtocoloItem;
    private Emolumento tabelaEmolumento;
    private String motivoIsencao;
    private ProtocoloItem protocoloItemDesvincular;
    private boolean permitirEdicaoObservacao;
    private boolean permitirAdicionarServicos;

    @PostConstruct
    public void init() {
        controlesalva = false;
        Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.configuracaoSistema = configuracaoSistemaServico.obterConfiguracao();
        this.editarId = parametros.get("protocoloId");
        this.parcela = new Parcela();
        this.parcelas = new ArrayList<Parcela>();
        this.protocolo = new Protocolo();
        this.protocoloItens = new ArrayList<ProtocoloItem>();
        this.protocolosItensRemovidos = new ArrayList<ProtocoloItem>();
        this.editarApenasAnexo = false;

        this.impostos = impostoServico.pesquisarImpostosVigente();

        emolumentoServico.obterEmolumentoAtivo();

        instanciarItens();
        instanciar();

        if (this.editarId != null) {
            messageErroNoAccess = "Você não possui permissão para editar protocolo.";
            delegar();
        } else {
            messageErroNoAccess = "Você não possui permissão para criar protocolo.";
            super.instanciar();
        }

        protocolo.gerarImpostosMap();
        for (Imposto imposto : impostos) {
            if (protocolo.getImpostosMap().get(imposto.getId()) == null) {
                this.protocolo.getImpostos().add(new ImpostoProtocolo(imposto));
            }
        }

        String isFirma = parametros.get("isfirma");
        String isProtesto = parametros.get("isprotesto");

        if (!Utils.isEmpty(isFirma) && processoFirmaAutenticacao == null) {
            processoFirmaAutenticacao = true;
        }

        if (isFirma != null) {
            String cliente = parametros.get("cliente");

            if (Utils.isNotEmpty(cliente)) {
                this.protocolo.setCliente(pessoaServico.load(Long.parseLong(cliente)));
            }

            protocoloFirma = true;
            //TODO PROTOCOLOS DE FIRMA POR PADRÃO COMPETENCIA FIRMA E AUTENTICACAO
            this.protocolo.setCompetencia(tipoLivroServico.getTipoFirma());
            if (this.protocolo.getCompetencia() != null) {
                this.protocolo.setProtocoloFirmaAutenticacaoReconhecimento(this.protocolo.getCompetencia().isTipoFirma());
            }

            //qtdfirma=2&qtdautenticacao=0&qtdarquivamento=0
            try {

                if ((parametros.get("qtdfirma") != null && !parametros.get("qtdfirma").equals("0"))
                        || (!Utils.isEmpty(parametros.get("firmas")) && !parametros.get("firmas").contains("null")
                        && !parametros.get("firmas").equals("-"))) {

                    this.quantidade = Long.parseLong(parametros.get("qtdfirma"));

                    if (this.quantidade == 0 || this.quantidade == null) {
                        String[] qtdfirma = parametros.get("firmas").split("-");
                        this.quantidade = Long.valueOf(qtdfirma.length);
                    }

                    if ((!configuracaoSistema.isUtilizaSeloDigital()
                            && !configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())
                            || parametros.get("firmas") == null) {

                        Long quantidadeGeral = this.quantidade;

                        if (this.quantidade > 0) {

                            if (Utils.isNotEmpty(parametros.get("firmas"))) {

                                String[] firmas = parametros.get("firmas").split("-");


                                for (String string : firmas) {

                                    ReconhecimentoFirma reconhecimentoFirma = reconhecimentoFirmaServico.load(Long.parseLong(string));

                                    if (Utils.isNotEmpty(reconhecimentoFirma.getItemReconhecimentoFirmas())) {
                                        quantidadeGeral = quantidadeGeral - reconhecimentoFirma.getItemReconhecimentoFirmas().size();
                                    } else {
                                        quantidadeGeral--;
                                    }


                                    List<Ato> atosFirma = atosFirma(reconhecimentoFirma);
                                    
                                    for (Ato ato1 : atosFirma) {
                                        
                                        this.ato = ato1;
                                        this.quantidade = Long.valueOf(reconhecimentoFirma.getQuantidade());
                                        this.gratuidadeItem = reconhecimentoFirma.isGratuito();
                                        
                                        gerarCampos();
                                        if (protocoloCampos != null && !protocoloCampos.isEmpty()) {
                                            String valor = "Reconhecimento por " + reconhecimentoFirma.getTipoReconhecimento().getNome() + " de ";
                                            for (ItemReconhecimentoFirma irf : reconhecimentoFirma.getItemReconhecimentoFirmas()) {
                                                
                                                if (irf.getFirma() != null && irf.getFirma().getPessoa() != null) {
                                                    valor += irf.getFirma().getPessoa().getNome() + ", ";
                                                }
                                            }
                                            protocoloCampos.get(0).setValor(valor);
                                        }
                                        
                                        adicionarProtocoloItem(true, Classe.RECONHECIMENTO_FIRMA, reconhecimentoFirma.getId(), reconhecimentoFirma.getBaseLegal());
                                        
                                    }

                                    //CORREÇÃO NOS VALORES

//                                    List<Ato> atosFirma = new ArrayList<Ato>();
//
//                                    for (ItemReconhecimentoFirma irf : reconhecimentoFirma.getItemReconhecimentoFirmas()) {
//
//                                        atosFirma = atosFirma(irf);
//
//                                        for (Ato ato1 : atosFirma) {
//
//                                            this.ato = ato1;
//                                            this.quantidade = Long.valueOf(reconhecimentoFirma.getQuantidade());
//                                            this.gratuidadeItem = reconhecimentoFirma.isGratuito();
//
//                                            adicionarProtocoloItem(true, Classe.RECONHECIMENTO_FIRMA, reconhecimentoFirma.getId(), reconhecimentoFirma.getBaseLegal());
//
//                                        }
//                                    }
                                    
                                    
                                }
                            }

                            if (quantidadeGeral > 0) {
                                this.ato = emolumentoItemServico.obterAtoReconhecimentoFirma(null);
                                this.quantidade = quantidadeGeral;
                                adicionarProtocoloItem();
                            }
                        }
                    }

                    //VINCULACAO E SELAGEM DE ATOS DE FIRMA
                    if (parametros.get("firmas") != null
                            && (configuracaoSistema.isUtilizaSeloDigital()
                            || configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())) {

                        String[] aut = parametros.get("firmas").split("-");

                        ArrayList<ProtocoloItem> pis = new ArrayList<ProtocoloItem>();
                        for (String string : aut) {
                            if (string != null && string.length() >= 1) {

                                ReconhecimentoFirma reconhecimentoFirma = reconhecimentoFirmaServico.load(Long.parseLong(string));

                                List<Ato> atosFirma = atosFirma(reconhecimentoFirma);

//                                if ((notarioCeara() || notarioMaranhao()) && Utils.isNotEmpty(reconhecimentoFirma.getItemReconhecimentoFirmas()) && reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).isDut()) {
//                                    atosFirma = emolumentoItemServico.obterAtosDut();
//                                } else if (Utils.isNotEmpty(reconhecimentoFirma.getItemReconhecimentoFirmas()) && reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).isFinanceiro()) {
//                                    atosFirma = emolumentoItemServico.obterAtoReconhecimentoFimaFinanceiro();
//                                } else {
//                                    atosFirma = emolumentoItemServico.obterAtosReconhecimentoFirma(reconhecimentoFirma.getTipoReconhecimento());
//                                }

                                for (Ato ato1 : atosFirma) {
                                    this.ato = ato1;
                                    this.quantidade = Long.valueOf(reconhecimentoFirma.getQuantidade());
                                    this.gratuidadeItem = reconhecimentoFirma.isGratuito();

                                    gerarCampos();
                                    if (protocoloCampos != null && !protocoloCampos.isEmpty()) {
                                        String valor = "Reconhecimento por " + reconhecimentoFirma.getTipoReconhecimento().getNome() + " de ";
                                        for (ItemReconhecimentoFirma irf : reconhecimentoFirma.getItemReconhecimentoFirmas()) {

                                            if (irf.getFirma() != null && irf.getFirma().getPessoa() != null) {
                                                valor += irf.getFirma().getPessoa().getNome() + ", ";
                                            }
                                        }
                                        protocoloCampos.get(0).setValor(valor);
                                    }

                                    adicionarProtocoloItem(true, Classe.RECONHECIMENTO_FIRMA, reconhecimentoFirma.getId(), reconhecimentoFirma.getBaseLegal());

                                }


                                //CORREÇÃO NOS VALORES
//                                List<Ato> atosFirma = new ArrayList<Ato>();
//
//                                for (ItemReconhecimentoFirma irf : reconhecimentoFirma.getItemReconhecimentoFirmas()) {
//
//                                    atosFirma = atosFirma(irf);
//
//                                    for (Ato ato1 : atosFirma) {
//
//                                        this.ato = ato1;
//                                        this.quantidade = Long.valueOf(reconhecimentoFirma.getQuantidade());
//                                        this.gratuidadeItem = reconhecimentoFirma.isGratuito();
//
//                                        adicionarProtocoloItem(true, Classe.RECONHECIMENTO_FIRMA, reconhecimentoFirma.getId(), reconhecimentoFirma.getBaseLegal());
//
//                                    }
//                                }

                                if (configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao()) {
                                    if (parametroSeloFirmaAutenticacao == null) {
                                        parametroSeloFirmaAutenticacao = "";
                                    }

                                    parametroSeloFirmaAutenticacao = "&selafirma=true";
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            //qtdfirma=2&qtdautenticacao=0&qtdarquivamento=0
            try {
                if ((parametros.get("qtddut") != null && !parametros.get("qtddut").equals("0"))
                        || (!Utils.isEmpty(parametros.get("dut")) && !parametros.get("dut").contains("null") && !parametros.get("dut").equals("-"))) {

                    this.ato = emolumentoItemServico.obterAtoDut();
                    this.quantidade = Long.parseLong(parametros.get("qtddut"));


                    if ((!configuracaoSistema.isUtilizaSeloDigital()
                            && !configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())
                            || parametros.get("dut") == null) {

                        if (this.quantidade > 0) {
                            List<Ato> listAtos = emolumentoItemServico.obterAtosDut();
                            for (Ato ato1 : listAtos) {
                                this.ato = ato1;
                                adicionarProtocoloItem();
                            }
                        }
                    }


                    //SELAGEM DO ATO DE DUT


                    // VINCULACAO E SELAGEM DE ATOS DE AUTENTICACAO
                    if (parametros.get("dut") != null
                            && (configuracaoSistema.isUtilizaSeloDigital()
                            || configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())) {

                        String[] dut = parametros.get("dut").split("-");

                        for (String string : dut) {
                            if (string != null && string.length() >= 1) {
                                this.quantidade = 1L;
                                this.ato = emolumentoItemServico.obterAtoDut();
                                DutEletronico dutEletronico = dutEletronicoServico.load(Long.parseLong(string));

                                this.gratuidadeItem = dutEletronico.isGratuito();

                                gerarCampos();
                                if (protocoloCampos != null && !protocoloCampos.isEmpty()) {
                                    protocoloCampos.get(0).setValor("Placa: " + dutEletronico.getPlaca() + (dutEletronico.getComprador() != null ? " - Comprador: " + dutEletronico.getComprador() : "") + (dutEletronico.getVendedor() != null ? " - Vendendor: " + dutEletronico.getVendedor() : ""));
                                }

                                adicionarProtocoloItem(true, Classe.DUT, dutEletronico.getId());

                                if (configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao()) {
                                    if (parametroSeloFirmaAutenticacao == null) {
                                        parametroSeloFirmaAutenticacao = "";
                                    }
                                    parametroSeloFirmaAutenticacao += "&seladut=true";
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            //qtdfirma=2&qtdautenticacao=0&qtdarquivamento=0&qtdatanotarial=1
            try {
                if (parametros.get("qtdatanotarial") != null && !parametros.get("qtdatanotarial").equals("0")) {
                    this.ato = emolumentoItemServico.obterAtaNotarialAutenticatoria();
                    this.quantidade = Long.parseLong(parametros.get("qtdatanotarial"));

                    if ((!configuracaoSistema.isUtilizaSeloDigital()
                            && !configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())
                            || (parametros.get("qtdatanotarial") == null)) {
                        if (this.quantidade > 0) {
                            adicionarProtocoloItem();
                        }
                    }

                    // VINCULACAO E SELAGEM DE ATOS DE AUTENTICACAO ATA NOTARIAL
                    if (this.quantidade > 0 && parametros.get("qtdatanotarial") != null
                            && (configuracaoSistema.isUtilizaSeloDigital()
                            || configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())) {

                        String[] aut = parametros.get("autenticacaoatanotarial").split("-");

                        for (String string : aut) {
                            if (string != null && string.length() >= 1) {

                                Autenticacao autenticacao = autenticacaoServico.load(Long.parseLong(string));
                                gratuidadeItem = autenticacao.isGratuito();

                                if (autenticacao.isAtaNotarialAutenticatoria()) {

                                    this.quantidade = 1L;
                                    this.ato = emolumentoItemServico.obterAtaNotarialAutenticatoria();

                                    gerarCampos();
                                    if (protocoloCampos != null && !protocoloCampos.isEmpty()) {
                                        protocoloCampos.get(0).setValor(autenticacao.getTipoAutenticacao().getTitulo() + (autenticacao.getDescricaoDocumento() != null ? " - " + autenticacao.getDescricaoDocumento() : ""));
                                    }

                                    adicionarProtocoloItem(true, Classe.AUTENTICACAO, autenticacao.getId(), autenticacao.getBaseLegal());

                                    if (configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao()) {
                                        if (parametroSeloFirmaAutenticacao == null) {
                                            parametroSeloFirmaAutenticacao = "";
                                        }
                                        parametroSeloFirmaAutenticacao += "&selaautenticacao=true";
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            //ADD RUBRICA
            try {
                if (parametros.get("qtdrubrica") != null && !parametros.get("qtdrubrica").equals("0")) {
                    this.ato = emolumentoItemServico.obterAtoRubrica();
                    this.quantidade = Long.parseLong(parametros.get("qtdrubrica"));
                    adicionarProtocoloItem();
                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            //ADD CARIMBO
            try {
                if (parametros.get("qtdcarimbo") != null && !parametros.get("qtdcarimbo").equals("0")) {
                    this.ato = emolumentoItemServico.obterAtoCarimbo();
                    this.quantidade = Long.parseLong(parametros.get("qtdcarimbo"));
                    adicionarProtocoloItem();
                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            try {
                if ((parametros.get("qtdautenticacao") != null && !parametros.get("qtdautenticacao").equals("0"))
                        || (!Utils.isEmpty(parametros.get("autenticacao")) && !parametros.get("autenticacao").contains("null")
                        && !parametros.get("autenticacao").equals("-"))) {

                    this.ato = emolumentoItemServico.obterAtoAutenticacao();
                    this.quantidade = Long.parseLong(parametros.get("qtdautenticacao"));

                    if (this.quantidade == 0 || this.quantidade == null) {
                        String[] qtdautent = parametros.get("autenticacao").split("-");
                        this.quantidade = Long.valueOf(qtdautent.length);
                    }

                    if ((!configuracaoSistema.isUtilizaSeloDigital()
                            && !configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())
                            || (parametros.get("autenticacao") == null)) {
                        if (this.quantidade > 0) {
                            adicionarProtocoloItem();
                        }
                    }

                    if (configuracaoSistema.isUtilizaSeloDigital() && !configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao()) {
                        if (this.quantidade > 0) {
                            adicionarProtocoloItem();
                        }
                    } else if (parametros.get("autenticacao") != null
                            && (configuracaoSistema.isUtilizaSeloDigital()
                            || configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao())) {

                        // VINCULACAO E SELAGEM DE ATOS DE AUTENTICACAO
                        List<Autenticacao> autenticacoes = autenticacaoServico.obterAutenticacoesPendentes(usuarioServico.getUsuarioLogado());

                        for (Autenticacao autenticacao : autenticacoes) {

                            if (!autenticacao.isAtaNotarialAutenticatoria()) {
                                gratuidadeItem = autenticacao.isGratuito();

                                if (!autenticacao.isAtaNotarialAutenticatoria()) {

                                    this.quantidade = 1L;
                                    this.ato = emolumentoItemServico.obterAtoAutenticacao();

                                    gerarCampos();
                                    if (protocoloCampos != null && !protocoloCampos.isEmpty()) {
                                        protocoloCampos.get(0).setValor(autenticacao.getTipoAutenticacao().getTitulo() + (autenticacao.getDescricaoDocumento() != null ? " - " + autenticacao.getDescricaoDocumento() : ""));
                                    }

                                    adicionarProtocoloItem(true, Classe.AUTENTICACAO, autenticacao.getId(), autenticacao.getBaseLegal());


                                }

                            }
                        }

                        if (configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao()) {
                            if (parametroSeloFirmaAutenticacao == null) {
                                parametroSeloFirmaAutenticacao = "";
                            }
                            parametroSeloFirmaAutenticacao += "&selaautenticacao=true";
                        }
                    }

                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            try {
                if (parametros.get("qtdarquivamento") != null && !parametros.get("qtdarquivamento").equals("0")) {
                    List<Ato> listAtosArquivamento = emolumentoItemServico.obterAtosAberturaFirma();

                    String[] arquivamentos = null;
                    if (!Utils.isEmpty(parametros.get("arquivamentos"))) {
                        arquivamentos = parametros.get("arquivamentos").split("-");
                    }

                    long quantidadeArquivamento = Long.parseLong(parametros.get("qtdarquivamento"));

                    for (Ato ato1 : listAtosArquivamento) {

                        if (arquivamentos != null && arquivamentos.length > 0) {
                            for (String string : arquivamentos) {
                                this.ato = ato1;
                                this.quantidade = 1l;
                                quantidadeArquivamento--;
                                if (!Utils.isEmpty(string) && string.trim().length() >= 1) {
                                    adicionarProtocoloItem(true, Classe.ARQUIVAMENTO_FIRMA, Long.parseLong(string));
                                }

                            }
                        }

                        if ((arquivamentos != null && arquivamentos.length > 0) || quantidadeArquivamento != 0) {
                            this.ato = ato1;
                            this.quantidade = quantidadeArquivamento;
                            adicionarProtocoloItem();
                        }
                    }

                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            try {
                if (parametros.get("qtdfotocopia") != null && !parametros.get("qtdfotocopia").equals("0")) {
                    this.ato = emolumentoItemServico.obterAtoFotocopia();
                    this.quantidade = Long.parseLong(parametros.get("qtdfotocopia"));
                    adicionarProtocoloItem();
                }
            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }

            salvar(true);

        } else if (isProtesto != null) {

            protocoloFirma = true;
            //TODO PROTOCOLOS DE PROTESTO POR PADRÃO COMPETENCIA PROTESTO
            this.protocolo.setCompetencia(tipoLivroServico.getTipoProtesto());

            //isprotesto=true&isprotesto=ID&apontamento=true
            try {
                Protesto protesto = protestoServico.pesquisar(Long.parseLong(parametros.get("protesto")));
                if (protesto != null) {

                    this.protocolo.setCliente(protesto.getApresentante());
                    this.protocolo.setLivro(protesto.getLivro());

                    if (parametros.get("apontamento") != null && !parametros.get("apontamento").equals("null")) {
                        List<Ato> atos = atoServico.obterAtoProtestoApontamento72horas();
                        for (Ato ato1 : atos) {
                            this.ato = ato1;
                            this.quantidade = 1L;
                            adicionarProtocoloItem(true, Classe.PROTESTO, protesto.getId());
                        }
                    }

                    registrarNoLivro();
                    salvar(false);


                    if (protocolo.getCodigoLivro() != null) {
                        protesto.setCodigo(Integer.parseInt(protocolo.getCodigoLivro() + ""));
                    }
                    protesto.setApontamento(protocolo);
                    protestoServico.atualizar(protesto);

                    Mensagem.messagemInfoRedirect("Protesto cadastrado com sucesso!", "visualizarProtesto.xhtml?protestoId=" + protesto.getId());
                }

            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError();
            }
        }
    }

    public void setarCliente(Pessoa pessoa) {
        this.protocolo.setCliente(pessoa);
        this.protocolo.setIssRetidoNaFonte(pessoa.isIssRetidoNaFonte());
        mostrarAvisoConsultarSubstitutoTributario();
    }

    public void verificarIss() {
        if (this.protocolo.getCliente() != null) {
            this.protocolo.setIssRetidoNaFonte(this.protocolo.getCliente().isIssRetidoNaFonte());
        }
        mostrarAvisoConsultarSubstitutoTributario();
        calcularDesconto();
        criarParcela();
    }

    public void setarRequerente(Pessoa pessoa) {
        this.protocolo.setRequerente(pessoa);
    }

    public void selecionarCompetencia() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("selecionarCompetencia.hide();");
    }

    @Override
    public void instanciar() {
        this.protocolo.setDataProtocolo(new Date());
        this.protocolo.setTipoProtocolo(TipoProtocolo.PROTOCOLO);
        this.protocolo.setValorCartorio(BigDecimal.ZERO);
        this.protocolo.setValorCartorioComDesconto(BigDecimal.ZERO);
        this.protocolo.setValorFundo(BigDecimal.ZERO);
        this.protocolo.setValorTotalSelo(BigDecimal.ZERO);
        this.protocolo.setISS(BigDecimal.ZERO);
        this.protocolo.setTramites(new ArrayList<Tramite>());
        this.protocolo.setParcelas(new ArrayList<Parcela>());
        this.protocolo.setAnexos(new ArrayList<Anexo>());
        this.protocolo.setTramites(new ArrayList<Tramite>());
        this.protocolo.setPartes(new ArrayList<Parte>());
        this.protocolo.setIssRetidoNaFonte(false);

        this.valorTotalProtocolo = BigDecimal.ZERO;
        this.valorTotalProtocoloSemDesconto = BigDecimal.ZERO;

        this.anexo = new Anexo();
        this.anexosRemovidos = new ArrayList<Anexo>();

        this.cartorio = cartorioServico.obterCartorio();

        this.parte = new Parte();
        this.parte.setPessoa(new Pessoa());
        this.tipoPessoaParte = TipoPessoa.FISICA;

        List<TipoLivro> competencias = tipoLivroServico.findAll();
        if (!Utils.isEmpty(competencias) && competencias.size() == 1) {
            this.protocolo.setCompetencia(competencias.get(0));
        }
    }

    public boolean isConfigracaoLimitarEdicaoProcesso() {
        return configuracaoSistemaServico.obterConfiguracao().isLimitarEdicaoProcesso();
    }

    @Override
    public void delegar() {

        if (configuracaoSistema.isLimitarEdicaoProcesso()) {
            this.administradorEdicaoProcesso = (configuracaoSistema.isLimitarEdicaoProcesso()
                    && !UserUtils.hasAccess(getUserSystem(), "PROTOCOLO", "ADMINISTRADOR EDITAR"));
        }

        this.limitarEdicaoProcesso = (configuracaoSistemaServico.obterConfiguracao().isLimitarEdicaoProcesso()
                && UserUtils.hasAccess(getUserSystem(), "PROTOCOLO", "COORDENADOR EDITAR"));

        if (this.editarId != null) {
            this.protocolo = protocoloServico.pesquisar(Long.parseLong(editarId));

            for (ProtocoloItem item : protocolo.getProtocoloItems()) {
                if (item.getAtoItem().isRequerClienteComCpf()) {
                    this.validarAtoComClienteNoProcesso = true;
                    break;
                }
            }

            if (protocolo.getTipoProtocolo().equals(TipoProtocolo.PROTOCOLO)) {
                super.delegar();
            } else {
                messageErroNoAccess = "visualizarProtocolo.xhtml?protocoloId=" + protocolo.getId();
                verifyPermission(UserUtils.Permission.SAVE);
                administradorEdicaoProcesso = false;
            }

//            if ((!this.protocolo.getStatusProtocolo().equals(StatusProtocolo.PRATICADO) || this.protocolo.isProtocoloFirmaAutenticacaoReconhecimento()) && !this.protocolo.getStatusProtocolo().equals(StatusProtocolo.CANCELADO)) {
            if (!this.protocolo.getStatusProtocolo().equals(StatusProtocolo.CANCELADO) || UserUtils.hasAccess(getUserSystem(), "PROTOCOLO", "EDITAR OBSERVACAO")) {

                this.protocolo.setProtocoloItems(protocoloServico.obterItens(this.protocolo, null));
                this.protocoloItens.addAll(this.protocolo.getProtocoloItems());
                this.gratuidadeItem = this.protocolo.isGratuidade();
                this.parcelas = protocoloServico.obterParcelas(this.protocolo);

                if (this.protocolo.getValorCartorioComDesconto() != null && this.protocolo.getValorFundo() != null) {
                    this.valorTotalProtocolo = this.protocolo.getValorCartorioComDesconto();//.add(this.protocolo.getValorFundo()).add(this.protocolo.getValorTotalSelo());
                    this.valorTotalProtocoloSemDesconto = this.protocolo.getValorCartorio().add(this.protocolo.getValorFundo()).add(this.protocolo.getValorTotalSelo()).add(valorImpostos());

                } else {
                    this.protocolo.setValorCartorioComDesconto(BigDecimal.ZERO);
                    this.protocolo.setValorFundo(BigDecimal.ZERO);
                    this.valorTotalProtocolo = BigDecimal.ZERO;
                    this.valorTotalProtocoloSemDesconto = BigDecimal.ZERO;
                }

                if (!Utils.isEmpty(this.protocolo.getProtocolosVinculados())) {
                    this.protocoloVinculado = this.protocolo.getProtocolosVinculados().get(0);
                }

                if (!Utils.isEmpty(this.protocolo.getStatusProtocolo())
                        && (this.protocolo.getStatusProtocolo().equals(StatusProtocolo.PRATICADO)
                        || this.protocolo.getStatusProtocolo().equals(StatusProtocolo.ENTREGUE)
                        || this.protocolo.getStatusProtocolo().equals(StatusProtocolo.CANCELADO))) {
                    this.editarApenasAnexo = true;
                }

                if (!Utils.isEmpty(this.protocolo.getStatusProtocolo())
                        && this.protocolo.getStatusProtocolo().equals(StatusProtocolo.PRATICADO)
                        && this.protocolo.isProtocoloFirmaAutenticacaoReconhecimento()
                        && !Utils.isEmpty(this.protocolo.getDataQuitacao())
                        && DateUtils.alterTime(this.protocolo.getDataQuitacao(), 0).equals(DateUtils.alterTime(new Date(), 0))) {
                    this.editarApenasAnexo = false;
                }

                if (this.protocolo.isRetificador()) {
                    this.editarApenasAnexo = true;
                }

                // Verifica a situação do processo e atribui o valor da configuração setada no painel do Notário para o atributo disable do campo
                if (this.protocolo.getStatusProtocolo().equals(StatusProtocolo.PRATICADO) || this.protocolo.getStatusProtocolo().equals(StatusProtocolo.ENTREGUE)) {
                    this.permitirEdicaoObservacao = !configuracaoSistema.isPermitirEditarObservacaoProcessosPraticados();
                    this.permitirAdicionarServicos = configuracaoSistema.isPermitirAdicionarServicos();
                    this.editarApenasAnexo = !configuracaoSistema.isPermitirAdicionarServicos();
                }

                this.impostos = impostoServico.pesquisarImpostosVigente();

                for (ProtocoloItem protocoloItem1 : protocoloItens) {
                    for (ImpostoProtocoloItem impostoProtocoloItem : protocoloItem1.getImpostos()) {
                        if (!this.impostos.contains(impostoProtocoloItem.getImposto())) {
                            this.impostos.add(impostoProtocoloItem.getImposto());
                        }
                    }
                }

            } else {
                try {
                    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                    context.redirect("visualizarProtocolo.xhtml?protocoloId=" + this.protocolo.getId());
                } catch (IOException ex) {
                    Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            anexoServico.ordenarPorOrdemInsercao(this.protocolo.getAnexos());
        }
    }

    public List<TipoLivro> autocompletarTipoLivro(String query) {
        return tipoLivroServico.autocompletar(query);
    }

    public List<ModeloProcesso> autocompletarModeloProcesso(String query) {
        if (configuracaoSistema.isSelecionarCompetenciaAntesDeCriarOProcesso()) {
            return modeloProcessoServico.autocompletar(query, this.protocolo.getCompetencia());
        } else {
            return modeloProcessoServico.autocompletar(query);
        }
    }

    public List<Protocolo> pesquisarProtocoloVinculado(String query) {

        try {

            Protocolo p = protocoloServico.pesquisarPorCodigo(Integer.parseInt(query));
            if (p != null) {
                return Arrays.asList(p);
            }
        } catch (java.lang.NumberFormatException e) {
            Mensagem.messagemError("Número inválido! Certifique-se que o campo Processo Vinculado possui apenas números.");
        }

        return null;
    }

    public void verificarProtocolo() throws NumberFormatException {
        if (this.protocolo.isProtocoloAntigo()) {
            if (!Utils.isEmpty(this.protocolo.getCodigoProtocoloAntigo())) {
                this.protocolo.setCodigo(Integer.parseInt(this.protocolo.getCodigoProtocoloAntigo()));
            } else if (this.protocolo.getCodigo() != null) {
                this.protocolo.setCodigoProtocoloAntigo(String.valueOf(this.protocolo.getCodigo()));
            }
        }
    }

    private void instanciarItens() {
        this.ato = new Ato();
        this.atoCampos = new ArrayList<AtoCampo>();
        this.protocoloCampos = new ArrayList<ProtocoloCampo>();
        if (!this.protocolo.isGratuidade()) {
            this.gratuidadeItem = false;
        }
        this.emolumentoItem = new EmolumentoItem();
        this.quantidade = 1L;
        this.valorCobradoProtocoloItemAnterior = BigDecimal.ZERO;
        this.cobrancaIndevidaProtocoloItem = false;
    }

    public String reprAto(Ato ato) {
        if (ato != null && ato.getCodigo() != null) {

            if (cartorio.getEndereco().getCidade().getEstado().getSigla().equals("PE")
                    && ato.getAtoTipo() != null
                    && !Utils.isEmpty(ato.getAtoTipo().getDescricao())
                    && !Utils.isEmpty(ato.getDescricao())) {
                return ato.getAtoTipo().getDescricao().concat("-[").concat(ato.getCodigo().concat("]-").concat(ato.getDescricao()));
            } else {
                return ato.getCodigo().concat(" - ").concat(ato.getDescricao());
            }
        } else {
            return "";
        }
    }

    public List<Ato> autocompletarAto(String query) {
        Ato atoPesquisar = new Ato();
        atoPesquisar.setCodigo(query);
        atoPesquisar.setSlug(query);
        atoPesquisar.setDescricao(query);

        if (this.configuracaoSistema.isSelecionarCompetenciaAntesDeCriarOProcesso()) {
            atoPesquisar.setAtoTipo(this.protocolo.getCompetencia().getAtoTipo());
        }

        if (this.tabelaEmolumento != null) {
            return atoServico.autocompletar(atoPesquisar, this.tabelaEmolumento);
        }

        return atoServico.autocompletarAtoNaTabelaEmolumentoAtiva(atoPesquisar);
    }

    public void obterValorTotal() {
        this.protocolo.setValorDevolvido(this.valorTotalProtocolo);
    }

    public void gerarCampos() {
        this.protocoloCampos = new ArrayList<ProtocoloCampo>();

        this.atoCampos = atoCampoServico.pesquisar(this.ato);

        for (AtoCampo campo : this.atoCampos) {
            ProtocoloCampo protocoloCampo = new ProtocoloCampo();
            protocoloCampo.setAtoCampo(campo);
            if (campo.isMinimoUmEmAtosDeQuantidade() && (campo.getAplicacao().equals(AplicacaoCampo.ADITIVO_PRE_DEFINIDO) || campo.getAplicacao().equals(AplicacaoCampo.ADITIVO_PRE_DEFINIDO_COM_FAXA))) {
                protocoloCampo.setValor("1");
            }

            this.protocoloCampos.add(protocoloCampo);
        }

        this.limitarQuantidadePrenotacao(this.ato);
    }

    public void setarGratuidade() {
        this.gratuidadeItem = this.protocolo.isGratuidade();
    }

    public void recalcularIss() {
        calcularDesconto();
        criarParcela();
    }

    public void setarIssRetidoNaFonte() {

        instanciarTotais();
        BigDecimal totalCartorio = BigDecimal.ZERO;
        ProtocoloItem protocoloItemValorRetido = null;
        for (ProtocoloItem item : this.protocoloItens) {
            calcularIssItem(item);
            item.setIssRetido(false);
            item.setValorRetido(BigDecimal.ZERO);

            if (protocoloItemValorRetido == null || item.getValor().compareTo(protocoloItemValorRetido.getValor()) > 0) {
                protocoloItemValorRetido = item;
            }

            BigDecimal valorCartorio = item.getValorCartorioBruto();
            if (protocolo.getDescontoPorcentagem() != null) {
                valorCartorio = RoundUtils.round(valorCartorio.subtract(valorCartorio.multiply(protocolo.getDescontoPorcentagem())));
            }

            valorCartorio = valorCartorio.multiply(new BigDecimal(item.getQuantidade()));

            totalCartorio = totalCartorio.add(valorCartorio);
            if (!item.isGratuidade()) {
                // total do fundo
                this.protocolo.setValorFundo(this.protocolo.getValorFundo().add(item.getValorFundo().multiply(new BigDecimal(item.getQuantidade()))));

                // total do cartório sem desconto
                this.protocolo.setValorCartorio(this.protocolo.getValorCartorio().add(item.getValorCartorio().multiply(new BigDecimal(item.getQuantidade()))));

//                if (!Utils.isEmpty(item.getImpostos())) {
//                    for (ImpostoProtocoloItem impostoItem : item.getImpostos()) {
//                        this.protocolo.setValorCartorio(this.protocolo.getValorCartorio().add(impostoItem.getValor()));
//                    }
//                }

                // total do cartório com desconto
                this.protocolo.setValorCartorioComDesconto(this.protocolo.getValorCartorio());

                // total dos selos
                this.protocolo.setValorTotalSelo(this.protocolo.getValorTotalSelo().add(item.getValorSelo().multiply(new BigDecimal(item.getQuantidade()))));
            }
        }

        calcularImpostosProtocoloItens(null);

        // sempre o valor do ISS vai ser em cima do valor total dos emolumento
        this.protocolo.setISS(totalCartorio.subtract(totalEmolumentoSemISS()).multiply(cartorio.getAliquotaIss()).setScale(2, BigDecimal.ROUND_FLOOR));
        if (this.protocolo.isIssRetidoNaFonte()) {
            this.protocolo.setValorCartorio(this.protocolo.getValorCartorio().subtract(this.protocolo.getISS()));
            if (protocoloItemValorRetido != null) {
                protocoloItemValorRetido.setValorRetido(this.protocolo.getISS());
                protocoloItemValorRetido.setIssRetido(true);
            }
        }
        this.valorTotalProtocoloSemDesconto = this.protocolo.getValorCartorio().add(this.protocolo.getValorTotalSelo()).add(this.protocolo.getValorFundo()).add(valorImpostos());
    }

    private BigDecimal totalEmolumentoSemISS() {

        BigDecimal emolumentos = BigDecimal.ZERO;

        for (ProtocoloItem item : this.protocoloItens) {
            if (!item.getAtoItem().isSomarNoIss() || item.isGratuidade()) {
                BigDecimal valorCartorio = item.getValorCartorioBruto();
                if (protocolo.getDescontoPorcentagem() != null) {
                    valorCartorio = RoundUtils.round(valorCartorio.subtract(valorCartorio.multiply(protocolo.getDescontoPorcentagem())));
                }

                valorCartorio = valorCartorio.multiply(new BigDecimal(item.getQuantidade()));
                emolumentos = emolumentos.add(valorCartorio);
            }
        }

        return emolumentos;
    }

    public BigDecimal calcularIssItem(ProtocoloItem protocoloItem) {
        ProtocoloItemHelper.calcularCampos(protocoloItem, protocoloItem.getEmolumentoItem(), configuracaoSistema);
        BigDecimal emolumento = protocoloItem.getValorCartorio();

        if (emolumento == null) {
            emolumento = BigDecimal.ZERO;
        }

        BigDecimal iss = emolumento.multiply(configuracaoSistema.getAliquotaIss()).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        iss = iss.multiply(new BigDecimal(protocoloItem.getQuantidade())).setScale(2, RoundingMode.HALF_EVEN);
        protocoloItem.setValorIss(iss);
        protocoloItem.setValor(protocoloItem.getValorCartorio().add(protocoloItem.getValorFundo()).add(protocoloItem.getValorSelo()));
        return iss;
    }

    public void adicionarProtocoloItem() {
        this.limitarQuantidadePrenotacao = false;
        adicionarProtocoloItem(null, null, null);
    }

    /**
     * Permite adicionar apenas uma prenotação por processo. Se houver um item
     * selado não permite mais adicionar outra Elaboração de Petição
     */
    public void adicionarProtocoloItem(Boolean vinculado, Classe classe, Long id) {
        adicionarProtocoloItem(vinculado, classe, id, null);
    }

    /**
     * Permite adicionar apenas uma prenotação por processo. Se houver um item
     * selado não permite mais adicionar outra Elaboração de Petição
     */
    public void adicionarProtocoloItem(Boolean vinculado, Classe classe, Long id, BaseLegal baseLegal) {

        Ato prenotacao = new Ato();
        Ato peticao = new Ato();

        prenotacao.setPrenotacao(true);
        peticao.setElaboracaoPeticao(true);

        List<Ato> atos = atoServico.pesquisar(prenotacao);
        if (!Utils.isEmpty(atos)) {
            prenotacao = atos.get(0);
        } else {
            prenotacao = null;
        }
//        boolean selarPrenotacao = false;

//        if (this.protocolo.getId() != null && (this.protocolo.getStatusProtocolo().equals(StatusProtocolo.PARCIALMENTE_PRATICADO) || this.protocolo.getStatusProtocolo().equals(StatusProtocolo.PRATICADO))) {
        atos = atoServico.pesquisar(peticao);
        if (!Utils.isEmpty(atos)) {
            peticao = atos.get(0);
        } else {
            peticao = null;
        }
//            selarPrenotacao = true;
//        }

        for (ProtocoloItem item : this.protocoloItens) {

            if (!configuracaoSistema.isPermitirMaisDeUmaElaboracaoDePeticaoPorProcesso() && peticao != null && item.getAtoItem().equals(this.ato) && this.ato.equals(peticao)) {
                instanciarItens();
                return;
            }

            if (!configuracaoSistema.isPermitirMaisDeUmaPrenotacaoPorProcesso() && item.getAtoItem().equals(this.ato) && this.ato.equals(prenotacao)) {
                instanciarItens();
                return;
            }
        }

        ProtocoloItem pi;

        if (this.ato.isRequerClienteComCpf() && !this.validarAtoComClienteNoProcesso) {
            validarAtoComClienteNoProcesso = true;
        }

        if (this.ato == null || this.ato.getId() == null) {
            return;
        }

        pi = new ProtocoloItem();

        pi.setDataAdicionado(new Date());
        pi.setValorCobradoProtocoloAnterior(valorCobradoProtocoloItemAnterior);
        pi.setCobrancaIndevida(cobrancaIndevidaProtocoloItem);
        pi.setGratuidade(this.gratuidadeItem);

        if (pi.isGratuidade() && !Utils.isEmpty(this.motivoIsencao)) {
            pi.setMotivoIsencao(this.motivoIsencao);
        }

        if (notarioCeara() && this.protocolo.isGratuidade()) {
            pi.setTipoGratuidade(this.protocolo.getTipoGratuidade());
        }
        if (pi.isGratuidade()) {
            pi.setTipoGratuidade(this.tipoGratuidadeProtocoloItem);
        }

        if (!pi.isGratuidade()) {
            pi.setTipoGratuidade(null);
        }

        if (baseLegal != null) {
            pi.setBaseLegal(baseLegal);
            if (Utils.isEmpty(protocoloCampos)) {

                ProtocoloCampo protocoloCampoBaseLegal = new ProtocoloCampo();
                protocoloCampoBaseLegal.setBaseLegal(baseLegal);
                AtoCampo atoCampo = new AtoCampo();
                atoCampo.setAplicacao(AplicacaoCampo.BASE_LEGAL);
                List<AtoCampo> resultado = atoCampoServico.pesquisar(atoCampo);
                if (!Utils.isEmpty(resultado)) {
                    protocoloCampoBaseLegal.setAtoCampo(resultado.get(0));
                    this.protocoloCampos.add(protocoloCampoBaseLegal);
                }
            } else {
                for (ProtocoloCampo protocoloCampo : protocoloCampos) {
                    if (protocoloCampo.getAtoCampo().getAplicacao().equals(AplicacaoCampo.BASE_LEGAL)) {
                        protocoloCampo.setBaseLegal(baseLegal);
                    }
                }
            }
        }
        if (quantidade > 0) {
            pi.setQuantidade(quantidade.intValue());
        } else {
            Mensagem.messagemWarn("Quantidade deve ser maior que 0 (zero).");
            return;
        }

        if (vinculado != null && vinculado) {
            pi.setVinculado(true);
            pi.setClasse(classe);
            pi.setIdVinculado(id);
        }

        boolean calculadoTotalEmolumentos = false;
        boolean rateioTJPEMaldito = false;
        // Ato com campos relacionados, trazer o item do emulomento correspondente
        // e com o valor dos campos
        if (!Utils.isEmpty(this.protocoloCampos)) {
            for (ProtocoloCampo protocoloCampo : this.protocoloCampos) {

                // Campo usado como filtro na tabela de emolumentos
                if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.FILTRO) {
                    try {
                        if (tabelaEmolumento != null) {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, new BigDecimal(protocoloCampo.getValor()), tabelaEmolumento);
                        } else {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, new BigDecimal(protocoloCampo.getValor()));
                        }
                        
                        if(!Utils.isEmpty(this.emolumentoItem.getValorTotal())){
                            pi.setValor(this.emolumentoItem.getValorTotal());
                        }                        

                        pi.setAtoItem(this.ato);

                        //TODO comentei por conta da tabela do MA
//                        if (emolumentoItem.getTipoCalculo() != null) {
                        pi.setEmolumentoItem(emolumentoItem);
//                        }

                        pi.setCodigoAto(this.emolumentoItem.getCodigoAto());

                        if (pi.getAtoItem().getTipoSelo() == null) {
                            pi.setSelado(true);
                        }

                        if (!this.protocoloItens.contains(pi)) {
                            this.protocoloItens.add(pi);
                        }
                        pi.setProtocoloCampos(this.protocoloCampos);
                        pi = calcularTotaisEmolumento(pi);

                        calculadoTotalEmolumentos = true;
                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato com o valor digitado.");
                        return;
                    }

                    // Campo usado como valor do ato
                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.VALOR) {

                    try {

                        if (this.tabelaEmolumento != null) {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, this.tabelaEmolumento);
                        } else {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato);
                        }

                        pi.setAtoItem(this.ato);

                        //TODO comentei por conta da tabela do MA
//                        if (emolumentoItem.getTipoCalculo() != null) {
                        pi.setEmolumentoItem(emolumentoItem);
//                        }

                        pi.setCodigoAto(this.emolumentoItem.getCodigoAto());
                        pi.setValor(new BigDecimal(protocoloCampo.getValor()));

                        if (pi.getAtoItem().getTipoSelo() == null) {
                            pi.setSelado(true);
                        }

                        if (!this.protocoloItens.contains(pi)) {
                            this.protocoloItens.add(pi);
                        }

                        calcularTotaisMaster(pi, protocoloCampo);
//                        pi = calcularImpostosProtocoloItem(pi);

                        calculadoTotalEmolumentos = true;
                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato.");
                    }

                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.VALOR_SEM_RATEIO) {

                    try {

                        if (this.tabelaEmolumento != null) {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, this.tabelaEmolumento);
                        } else {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato);
                        }

                        pi.setAtoItem(this.ato);

//                        //TODO comentei por conta da tabela do MA
//                        if (emolumentoItem.getTipoCalculo() != null) {
                        pi.setEmolumentoItem(emolumentoItem);
//                        }

                        pi.setCodigoAto(this.emolumentoItem.getCodigoAto());
                        pi.setValor(new BigDecimal(protocoloCampo.getValor()));

                        if (pi.getAtoItem().getTipoSelo() == null) {
                            pi.setSelado(true);
                        }

                        if (!this.protocoloItens.contains(pi)) {
                            this.protocoloItens.add(pi);
                        }

                        calcularTotaisMaster(pi, protocoloCampo);
//                        pi = calcularImpostosProtocoloItem(pi);

                        calculadoTotalEmolumentos = true;
                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato.");
                        return;
                    }

                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.VALOR_CARTORIO) {

                    try {

                        if (this.tabelaEmolumento != null) {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, this.tabelaEmolumento);
                        } else {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato);
                        }

                        pi.setAtoItem(this.ato);

                        //TODO comentei por conta da tabela do MA
//                        if (emolumentoItem.getTipoCalculo() != null) {
                        pi.setEmolumentoItem(emolumentoItem);
//                        }

                        pi.setCodigoAto(this.emolumentoItem.getCodigoAto());
                        pi.setValor(new BigDecimal(protocoloCampo.getValor()));

                        if (pi.getAtoItem().getTipoSelo() == null) {
                            pi.setSelado(true);
                        }

                        if (!this.protocoloItens.contains(pi)) {
                            this.protocoloItens.add(pi);
                        }

                        calcularTotaisMaster(pi, protocoloCampo);
//                        pi = calcularImpostosProtocoloItem(pi);

                        calculadoTotalEmolumentos = true;
                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato.");
                        return;
                    }

                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.ADITIVO_PRE_DEFINIDO
                        || protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.ADITIVO_PRE_DEFINIDO_COM_FAXA) {

                    try {

                        if (this.tabelaEmolumento != null) {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, this.tabelaEmolumento);
                        } else {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato);
                        }

                        pi.setAtoItem(this.ato);

                        pi.setEmolumentoItem(emolumentoItem);

                        pi.setValorCartorio(emolumentoItem.getValorCartorio());
                        pi.setValorFundo(emolumentoItem.getValorFundo());
                        pi.setCodigoAto(this.emolumentoItem.getCodigoAto());

                        if (pi.getAtoItem().getTipoSelo() == null) {
                            pi.setSelado(true);
                        }
                        if (!this.protocoloItens.contains(pi)) {
                            this.protocoloItens.add(pi);
                        }

                        if (protocoloCampo.getAtoCampo().isMinimoUmEmAtosDeQuantidade()) {
                            if (Utils.isEmpty(protocoloCampo.getValor())
                                    || Integer.valueOf(protocoloCampo.getValor()) == 0) {
                                protocoloCampo.setValor("1");
                            }
                        }

                        calcularTotaisMaster(pi, protocoloCampo);

                        calculadoTotalEmolumentos = true;
                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato.");
                        return;
                    }
                    rateioTJPEMaldito = true;
                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.FILTRO_EMOLUMENTO) {

                    try {
                        if (tabelaEmolumento != null) {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, new BigDecimal(protocoloCampo.getValor()), tabelaEmolumento);
                        } else {
                            this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, new BigDecimal(protocoloCampo.getValor()));
                        }

                        pi.setAtoItem(this.ato);

//                        //TODO comentei por conta da tabela do MA
//                        if (emolumentoItem.getTipoCalculo() != null) {
                        pi.setEmolumentoItem(emolumentoItem);
//                        }

                        pi.setValorCartorio(emolumentoItem.getValorCartorio());
                        if (pi.getValorFundo() == null) {
                            pi.setValorFundo(BigDecimal.ZERO);
                        }
                        pi.setCodigoAto(this.emolumentoItem.getCodigoAto());

                        if (pi.getAtoItem().getTipoSelo() == null) {
                            pi.setSelado(true);
                        }

                        if (!this.protocoloItens.contains(pi)) {
                            this.protocoloItens.add(pi);
                        }
                        pi.setProtocoloCampos(this.protocoloCampos);
                        calcularTotaisMaster(pi, protocoloCampo);

                        calculadoTotalEmolumentos = true;
                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato com o valor digitado.");
                        return;
                    }

                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.FILTRO_IMPOSTO) {
                    try {
                        if (!this.protocoloItens.contains(pi)) {
                            this.protocoloItens.add(pi);
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato com o valor digitado.");
                        return;
                    }
                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.BASE_LEGAL) {
                    if (protocoloCampo.getBaseLegal() != null && protocoloCampo.getBaseLegal().getId() == null) {
                        pi.setBaseLegal(null);
                    } else {
                        pi.setBaseLegal(protocoloCampo.getBaseLegal());
                    }
                }
            }

            //this.protocoloCampos.removeAll(campoRemocao);
            pi.setProtocoloCampos(this.protocoloCampos);
        }

        // Ato sem campos relacionados, trazer o item do emulomento correspondente
        if (Utils.isEmpty(this.protocoloCampos) || !calculadoTotalEmolumentos) {
            pi = calcularProtocoloItem(pi);
        }

        // Efetua o rateio do valor de cada campo.
        if (configuracaoSistema.isRatearEmolumentoEFundo()) {
            BigDecimal valorEmolumento = pi.getValorCartorio();
            BigDecimal valorFundoRateio = RoundUtils.round(valorEmolumento.multiply(configuracaoSistema.getPercentualImposto()));
            BigDecimal valorCartorioRateio;
            // Correção porque o TJPE não sabe fazer um arredondamento
            if (rateioTJPEMaldito) {
                valorCartorioRateio = RoundUtils.round(valorEmolumento.multiply(configuracaoSistema.getPercentualCartorio()));
                valorFundoRateio = valorEmolumento.subtract(valorCartorioRateio);
            } else {
                valorCartorioRateio = valorEmolumento.subtract(valorFundoRateio);
            }
            pi.setValorCartorio(valorCartorioRateio.subtract(pi.getValorRedutorEmolumento()));
            pi.setValorFundo(valorFundoRateio);

            pi.setValorCartorioBruto(valorCartorioRateio);
            pi.setValorFundoBruto(valorFundoRateio);
        } else {
            pi.setValorCartorioBruto(pi.getValorCartorio());
            pi.setValorFundoBruto(pi.getValorFundo());
        }

        // Calcular o desconto utilizando a base legal, lembrando que o ato já deve estar totalmente calculado.
        if (pi.getBaseLegal() != null) {

            BaseLegalUtils.calcularProtocoloItem(pi, configuracaoSistema);
        } else {
            if (!Utils.isEmpty(pi.getProtocoloCampos())) {

                for (Iterator<ProtocoloCampo> it = pi.getProtocoloCampos().iterator(); it.hasNext();) {
                    ProtocoloCampo protocoloCampo = it.next();

                    if (protocoloCampo.getAtoCampo().getAplicacao().equals(AplicacaoCampo.BASE_LEGAL)) {
                        pi.getProtocoloCampos().remove(protocoloCampo);
                        break;
                    }
                }
            }
        }

        calcularDesconto();
        criarParcela();

        protocoloItem = pi;

        instanciarItens();

        if (!this.protocoloItens.isEmpty() && this.gratuitoBooleanButton != null) {
            this.gratuitoBooleanButton.setDisabled(true);
        }
    }

    private ProtocoloItem calcularProtocoloItem(ProtocoloItem pi) {
        try {

            if (this.tabelaEmolumento != null && this.tabelaEmolumento.getId() != null) {
                this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato, this.tabelaEmolumento);
            } else {
                this.emolumentoItem = emolumentoItemServico.pesquisar(this.ato);
            }

            if (this.emolumentoItem.getValorTotal() != null) {
                pi.setValor(this.emolumentoItem.getValorTotal());
            } else {
                pi.setValor(BigDecimal.ZERO);
            }

            pi.setAtoItem(this.ato);
            pi.setCodigoAto(this.emolumentoItem.getCodigoAto());

            pi.setEmolumentoItem(this.emolumentoItem);

            if (pi.getAtoItem().getTipoSelo() == null) {
                pi.setSelado(true);
            }

            if (!this.protocoloItens.contains(pi)) {
                this.protocoloItens.add(pi);
            }

            calcularTotaisEmolumento(pi);
        } catch (Exception e) {
            Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato.");
        }

        return pi;
    }

    public void atualizarPagamento() {
        this.parcela.setQuitada(true);
        this.parcela.setDataPagamento(new Date());
        this.parcela.setFormaPagamento(FormaPagamento.ESPECIE);
        this.parcela.setValor(aReceber());
    }

    public ProtocoloItem calcularTotaisEmolumento(ProtocoloItem pi) {
        // verificar se não há valores na tabela de emolumentos para o ato,
        // se não houver utiliza-se 0
        if (this.emolumentoItem.getValorCartorio() != null) {
            pi.setValorCartorio(this.emolumentoItem.getValorCartorio());
        } else {
            pi.setValorCartorio(BigDecimal.ZERO);
        }
        if (this.emolumentoItem.getValorFundo() != null) {
            pi.setValorFundo(this.emolumentoItem.getValorFundo());
        } else {
            pi.setValorFundo(BigDecimal.ZERO);
        }

        Emolumento emolumento = this.emolumentoServico.obterEmolumentoAtivo();

        if (emolumento != null && emolumento.getId() != null
                && pi.getAtoItem() != null
                && (pi.getAtoItem().isPadrao()
                || pi.getAtoItem().isArquivamento()
                || pi.getAtoItem().isReconhecimentoFirma()
                || pi.getAtoItem().isAutenticacao()
                || pi.getAtoItem().isProtesto())) {

            if (configuracaoSistema != null && configuracaoSistema.isValorSeloPorTipo()
                    && pi.getAtoItem() != null && pi.getAtoItem().getTipoSelo() != null
                    && pi.getAtoItem().getTipoSelo().getValorSelo() != null) {

                pi.setValorSelo(pi.getAtoItem().getTipoSelo().getValorSelo());
            } else {
                pi.setValorSelo(emolumento.getValorSelo());
            }
            pi.setValor(pi.getValor().add(pi.getValorSelo()));

        } else if (configuracaoSistema != null && configuracaoSistema.isValorSeloPorTipo()
                && pi.getAtoItem() != null && pi.getAtoItem().getTipoSelo() == null
                && pi.getAtoItem().getValorSelo() != null) {
            pi.setValorSelo(pi.getAtoItem().getValorSelo());
        } else {
            pi.setValorSelo(BigDecimal.ZERO);
        }

        if (configuracaoSistema != null && configuracaoSistema.isUtilizarValorSeloAto()
                && pi.getAtoItem() != null && pi.getAtoItem().getValorSelo() != null
                && pi.getAtoItem().getValorSelo().compareTo(BigDecimal.ZERO) > 0) {
            pi.setValorSelo(pi.getAtoItem().getValorSelo());
        }

        // quando o item for gratuito, seta gratuidade =  true
        if (pi.getAtoItem().isGratuidade()) {
            pi.setGratuidade(true);
        }

        // --- CALCULOS CE AQUI
        calcularImpostosProtocoloItens(pi);

        // caso o item ou o protocolo não seja gratuito, adicionar os valores
        if (!this.protocolo.isGratuidade() && !pi.isGratuidade()) {
            calcularTotais(pi);
        } else {
            pi.setValorCartorio(BigDecimal.ZERO);
            pi.setValorFundo(BigDecimal.ZERO);
            pi.setValorSelo(BigDecimal.ZERO);
            pi.setValor(BigDecimal.ZERO);
            pi.setAtoItem(ato);
            pi.setValorIss(BigDecimal.ZERO);
        }
        return pi;
    }

    public void calcularTotaisMaster(ProtocoloItem pi, ProtocoloCampo protocoloCampo) {
        // Seta o valor do selo
        Emolumento emolumento = this.emolumentoServico.obterEmolumentoAtivo();
        if (emolumento != null && emolumento.getId() != null
                && pi.getAtoItem() != null
                && (pi.getAtoItem().isPadrao()
                || pi.getAtoItem().isAberturaFirma()
                || pi.getAtoItem().isArquivamento()
                || pi.getAtoItem().isReconhecimentoFirma()
                || pi.getAtoItem().isAutenticacao()
                || pi.getAtoItem().isProtesto())) {

            if (configuracaoSistema != null && configuracaoSistema.isValorSeloPorTipo()
                    && pi.getAtoItem() != null && pi.getAtoItem().getTipoSelo() != null) {
                pi.setValorSelo(pi.getAtoItem().getTipoSelo().getValorSelo());
            } else {
                pi.setValorSelo(emolumento.getValorSelo());
            }

        } else if (configuracaoSistema != null && configuracaoSistema.isValorSeloPorTipo()
                && pi.getAtoItem() != null && pi.getAtoItem().getTipoSelo() == null
                && pi.getAtoItem().getValorSelo() != null) {
            pi.setValorSelo(pi.getAtoItem().getValorSelo());
        } else {
            pi.setValorSelo(BigDecimal.ZERO);
        }


        if (configuracaoSistema != null && configuracaoSistema.isUtilizarValorSeloAto()
                && pi.getAtoItem() != null && pi.getAtoItem().getValorSelo() != null
                && pi.getAtoItem().getValorSelo().compareTo(BigDecimal.ZERO) > 0) {
            pi.setValorSelo(pi.getAtoItem().getValorSelo());
        }

        // calcula os valores referentes ao cartório e ao fundo de acordo com as porcentagems
        if (protocoloCampo.getAtoCampo().getAplicacao().equals(AplicacaoCampo.VALOR)) {
            pi.setValorCartorio(pi.getValor().multiply(this.cartorio.getPercentualCartorio()));
            pi.setValorFundo(pi.getValor().multiply(this.cartorio.getPercentualImposto()));
        } else if (protocoloCampo.getAtoCampo().getAplicacao().equals(AplicacaoCampo.VALOR_SEM_RATEIO)) {
            pi.setValorCartorio(pi.getValor());
            pi.setValorFundo(pi.getValor().multiply(this.cartorio.getPercentualImposto()));
            pi.setValor(pi.getValorCartorio().add(pi.getValorFundo()));
        } else if (protocoloCampo.getAtoCampo().getAplicacao().equals(AplicacaoCampo.VALOR_CARTORIO)) {
            pi.setValorCartorio(pi.getValor());
            pi.setValorFundo(BigDecimal.ZERO);
        } else if (protocoloCampo.getAtoCampo().getAplicacao().equals(AplicacaoCampo.ADITIVO_PRE_DEFINIDO)) {
            BigDecimal valor = new BigDecimal(protocoloCampo.getValor());
            EmolumentoItem itemEmolumento = pi.getEmolumentoItem();
            if (protocoloCampo.getAtoCampo().isDesconsiderarPrimeiroValor()) {
                // Não pode contar o primeiro ( FOLHAS ), só as demais.
                valor = valor.subtract(BigDecimal.ONE);
            }

            if (valor.compareTo(BigDecimal.ZERO) > 0) {
                pi.setValorCartorio(pi.getValorCartorio().add(valor.multiply(itemEmolumento.getValorAditivo())));
            }

            // acrescimo do fundo
            if (itemEmolumento.getTipoAditivoFundo() != null) {
                if (itemEmolumento.getValorAditivoFundo() != null) {

                    if (itemEmolumento.getTipoAditivoFundo().equals(TipoDesconto.DINHEIRO)) {
                        pi.setValorFundo(pi.getValorFundo().add(valor.multiply(itemEmolumento.getValorAditivoFundo())));
                    } else {
                        BigDecimal valorDoAditivo = emolumentoItem.getValorAditivo().multiply(itemEmolumento.getValorAditivoFundo().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_EVEN);
                        pi.setValorFundo(pi.getValorFundo().add(valor.multiply(valorDoAditivo)));
                    }
                }
            }

            // Acrescimo do selo
            if (itemEmolumento.getValorAditivoSelo() != null) {
                pi.setValorSelo(pi.getValorSelo().add(valor.multiply(itemEmolumento.getValorAditivoSelo())));
            }

            if (pi.getEmolumentoItem().getValorMaximoAto() != null) {
                if (pi.getValorCartorio().compareTo(pi.getEmolumentoItem().getValorMaximoAto()) > 0) {
                    pi.setValorCartorio(pi.getEmolumentoItem().getValorMaximoAto());
                }
            }
        } else if (protocoloCampo.getAtoCampo().getAplicacao().equals(AplicacaoCampo.ADITIVO_PRE_DEFINIDO_COM_FAXA)) {
            BigDecimal valor = new BigDecimal(protocoloCampo.getValor());
            if (valor.compareTo(BigDecimal.ZERO) > 0) {
                pi.setValorCartorio(pi.getValorCartorio().add(valor.multiply(pi.getEmolumentoItem().getValorAditivo())));
            }
            if (pi.getEmolumentoItem().getValorMaximoAto() != null) {
                if (pi.getValorCartorio().compareTo(pi.getEmolumentoItem().getValorMaximoAto()) > 0) {
                    pi.setValorCartorio(pi.getEmolumentoItem().getValorMaximoAto());
                } else if (pi.getValorCartorio().compareTo(pi.getEmolumentoItem().getValorMinimoAto()) < 0) {
                    pi.setValorCartorio(pi.getEmolumentoItem().getValorMinimoAto());
                }
            }
        }

        pi.setValor(pi.getValorFundo().add(pi.getValorCartorio()).add(pi.getValorSelo()));

        // caso o item ou o protocolo não seja gratuito, adicionar os valores
        if (!this.protocolo.isGratuidade() && !pi.isGratuidade()) {
            calcularTotais(pi);
        }
    }

    private void instanciarTotais() {
        // total do fundo
        this.protocolo.setValorFundo(BigDecimal.ZERO);

        // total do cartório sem desconto
        this.protocolo.setValorCartorio(BigDecimal.ZERO);

        // total do cartório com desconto
        this.protocolo.setValorCartorioComDesconto(BigDecimal.ZERO);

        // total dos selos
        this.protocolo.setValorTotalSelo(BigDecimal.ZERO);

        // calcular ISS
        this.protocolo.setISS(BigDecimal.ZERO);

        this.protocolo.setEmolumentosComDesconto(BigDecimal.ZERO);

        this.protocolo.setFundoComDesconto(BigDecimal.ZERO);

    }

    private void calcularTotais(ProtocoloItem pi) {
        // total do fundo
        this.protocolo.setValorFundo(this.protocolo.getValorFundo().add(pi.getValorFundo().multiply(new BigDecimal(pi.getQuantidade()))));

        // total do cartório sem desconto
        this.protocolo.setValorCartorio(this.protocolo.getValorCartorio().add(pi.getValorCartorio().multiply(new BigDecimal(pi.getQuantidade()))));

        // total do cartório com desconto
        this.protocolo.setValorCartorioComDesconto(this.protocolo.getValorCartorio());

        // total dos selos
        this.protocolo.setValorTotalSelo(this.protocolo.getValorTotalSelo().add(pi.getValorSelo().multiply(new BigDecimal(pi.getQuantidade()))));

        // calcular ISS
        setarIssRetidoNaFonte();

    }

    public boolean desabilitarRemoverItem(ProtocoloItem item) {
        return (item.isSelado() && item.getAtoItem().getTipoSelo() != null)
                || (item.isSelado() && item.getAtoItem().isSelagemManual() && item.getDataConversao() != null)
                || (notarioPiaui() && item.isVinculado() && item.getClasse() != null && item.getIdVinculado() != null)
                || (item.getDataConversao() != null && item.getAtoItem().isConverterAoProtocolar())
                || (configuracaoSistema.isBloquearRemocaoAtosConversaoImediata() && item.getDataConversao() != null && this.protocolo.getId() != null)
                || (item.isVinculado());
    }

    public void removerProtocoloItem(ProtocoloItem item) {

//        if (!item.isSelado() && !item.isVinculado()) {
//            
        if ((!item.isSelado() && !item.isVinculado())
                || (item.isSelado() && item.getAtoItem().getTipoSelo() == null)
                || (!item.isSelado() && item.isVinculado() && item.getProtocolo().getCompetencia().getDescricao().toLowerCase().contains("protesto"))
                || (!item.isSelado() && item.isVinculado() && item.getProtocolo().getCompetencia().getDescricao().toLowerCase().contains("firma"))) {

            // caso o item ou o protocolo não seja gratuito, subtrair os valores
            if (!this.protocolo.isGratuidade() && !item.isGratuidade()) {

                if (item.getValorFundo() != null) {
                    // total do fundo
                    this.protocolo.setValorFundo(this.protocolo.getValorFundo().subtract(item.getValorFundo().multiply(new BigDecimal(item.getQuantidade()))));
                }

                if (item.getValorCartorio() != null) {
                    // total do cartório sem desconto
                    this.protocolo.setValorCartorio(this.protocolo.getValorCartorio().subtract(item.getValorCartorio().multiply(new BigDecimal(item.getQuantidade()))));

                    // total do cartório com desconto, mesmo valor sem desconto
                    this.protocolo.setValorCartorioComDesconto(this.protocolo.getValorCartorio());
                }

                if (item.getValorSelo() != null) {
                    // total do selo
                    this.protocolo.setValorTotalSelo(this.protocolo.getValorTotalSelo().subtract(item.getValorSelo().multiply(new BigDecimal(item.getQuantidade()))));
                }

            }

            this.protocoloItens.remove(item);
            this.protocolosItensRemovidos.add(item);

            // atualizarPagamento();
            calcularDesconto();
            criarParcela();

        } else {
            Mensagem.messagemWarn("Não é possivel exluír este item!");
        }

        if (this.protocoloItens.isEmpty()) {
            this.gratuitoBooleanButton.setDisabled(false);
        }

    }

    public void calcularDesconto() {
        setarIssRetidoNaFonte();

        BigDecimal totalCartorioComDesconto = BigDecimal.ZERO;
        BigDecimal totalFundoComDesconto = BigDecimal.ZERO;
        BigDecimal totalSeloComDesconto = BigDecimal.ZERO;

        if (this.protocolo.getTipoDesconto() == TipoDesconto.DINHEIRO) {
            this.protocolo.setValorCartorioComDesconto(this.protocolo.getValorCartorioComDesconto().subtract(this.protocolo.getDescontoDinheiro()));
        } else if (this.protocolo.getTipoDesconto() == TipoDesconto.PORCENTAGEM) {

            BigDecimal desconto = this.protocolo.getDescontoPorcentagem();

            for (ProtocoloItem item : this.protocoloItens) {

                if (!item.isGratuidade()) {

                    BigDecimal qtd = new BigDecimal(item.getQuantidade());

                    BigDecimal valorCartorioItem = item.getValorCartorio();
                    BigDecimal valorFundoItem = item.getValorFundo();
                    BigDecimal valorSeloItem = item.getValorSelo();

                    item.setDesconto(desconto);
                    calcularImpostosProtocoloItens(item);

                    totalCartorioComDesconto = totalCartorioComDesconto.add(RoundUtils.round(valorCartorioItem.subtract(valorCartorioItem.multiply(desconto))).multiply(qtd));
                    totalFundoComDesconto = totalFundoComDesconto.add(RoundUtils.round(valorFundoItem.subtract(valorFundoItem.multiply(desconto))).multiply(qtd));

                    if (this.protocolo.getTipoProgramaProcesso().isAplicarNoSelo()) {
                        totalSeloComDesconto = totalSeloComDesconto.add(RoundUtils.round(valorSeloItem.subtract(valorSeloItem.multiply(desconto))).multiply(qtd));
                    } else {
                        totalSeloComDesconto = totalSeloComDesconto.add(valorSeloItem.multiply(qtd));
                    }
                }

            }

            this.protocolo.setEmolumentosComDesconto(totalCartorioComDesconto);
            this.protocolo.setFundoComDesconto(totalFundoComDesconto);

            this.protocolo.setValorCartorioComDesconto(totalCartorioComDesconto.add(totalFundoComDesconto).add(valorImpostosComDesconto()));
            if (desconto.compareTo(new BigDecimal(1)) != 0) {
                this.protocolo.setValorCartorioComDesconto(this.protocolo.getValorCartorioComDesconto().add(totalSeloComDesconto));
                if (this.protocolo.isIssRetidoNaFonte()) {
                    this.protocolo.setValorCartorioComDesconto(this.protocolo.getValorCartorioComDesconto().subtract(protocolo.getISS()));
                }
            }

        } else {

            for (ProtocoloItem item : this.protocoloItens) {
                item.setDesconto(BigDecimal.ZERO);
                calcularImpostosProtocoloItens(item);
            }


            this.protocolo.setEmolumentosComDesconto(protocolo.getValorCartorio());
            this.protocolo.setFundoComDesconto(protocolo.getValorFundo());
            BigDecimal valorImpostosComDesconto = valorImpostosComDesconto();
            this.protocolo.setValorCartorioComDesconto(this.protocolo.getValorCartorio().add(this.protocolo.getValorFundo()).add(this.protocolo.getValorTotalSelo()).add(valorImpostosComDesconto));
        }

        this.valorTotalProtocolo = this.protocolo.getValorCartorioComDesconto();
        this.valorTotalProtocoloSemDesconto = this.protocolo.getValorCartorio().add(this.protocolo.getValorTotalSelo()).add(this.protocolo.getValorFundo()).add(valorImpostos());
        criarParcela();
    }

    public void limparDesconto() {
        if (this.protocolo.getValorCartorio() != null) {
            this.protocolo.setDescontoDinheiro(null);
            this.protocolo.setDescontoPorcentagem(BigDecimal.ZERO);
            calcularDesconto();
        }

        criarParcela();
    }

    public void setarTipoDesconto() {
        this.protocolo.setTipoDesconto(null);
        atualizarPrograma();
    }

    public boolean verificarAdesaoParcela() {
        return aReceber().compareTo(BigDecimal.ZERO) == 0;
    }

    public BigDecimal aReceber() {
        BigDecimal valorPago = BigDecimal.ZERO;
        for (Parcela p : this.parcelas) {
            if (p.isQuitada()) {
                valorPago = valorPago.add(p.getValor());
            }
        }
        return this.valorTotalProtocolo.subtract(valorPago);
    }

    public void adicionarParcela() {


        if (this.parcela.isQuitada()) {
            this.parcela.setTipo(TipoFinanceiro.ENTRADA);
            this.parcela.setOperador(usuarioServico.getUsuarioLogado());
            this.parcela.setGerador(usuarioServico.getUsuarioLogado());
            this.parcela.setDataVencimento(null);
            this.parcela.setDataPagamento(new Date());
        } else {
            this.parcela.setGerador(usuarioServico.getUsuarioLogado());
            this.parcela.setDataPagamento(null);
            this.parcela.setTipo(null);
        }

        if (this.parcela.getValor().compareTo(aReceber()) <= 0) {
            this.parcelas.add(this.parcela);
            this.parcela = new Parcela();
            aReceber();
        } else {
            this.troco = this.parcela.getValor().subtract(aReceber());
            this.parcela.setValor(aReceber());
            this.parcelas.add(this.parcela);
            this.parcela = new Parcela();
            aReceber();

            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("trocoDialog.show();");
        }
    }

    public String itemDescricao(ProtocoloItem item) {
        return ProtocoloItemHelper.itemDescricao(item);
    }

    public void pagarParcela(Parcela parcela) {
        String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        parcela.setOperador(usuarioServico.obterUsuarioPorLogin(login));

        this.parcelas.remove(parcela);
        parcela.setQuitada(true);
        this.parcelas.add(parcela);
    }

    public void removerParcela(Parcela parcela) {
        this.parcelas.remove(parcela);
        if (Utils.isEmpty(this.parcelas)) {
            atualizarPagamento();
        }
    }

    public void limparParcela() {
        this.parcela = new Parcela();
    }

    public void criarParcela() {

        if (protocolo.getTipoProtocolo() != null && protocolo.getTipoProtocolo() == TipoProtocolo.ORCAMENTO) {
            return;
        }

        BigDecimal valorDiferenca = BigDecimal.ZERO;
        boolean novo = true;


        if (this.protocolo.getParcelas() != null && this.protocolo.getParcelas().isEmpty()) {
            novo = true;
        }

        for (Parcela parcela1 : this.protocolo.getParcelas()) {

            if (parcela1.isQuitada()) {
                valorDiferenca = valorDiferenca.add(parcela1.getValor());
            } else {
                this.parcela = parcela1;
                novo = false;
            }

        }

        valorDiferenca = valorDiferenca.divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);

        if (novo) {
            this.parcela = new Parcela();
        }

        this.parcela.setNumero(1);

        //CORRRIGI PARA DUAS CASAS DECIMAIS
        if (this.parcela.getValor() != null && this.parcela.getValor().compareTo(BigDecimal.ZERO) != 0) {
            this.parcela.setValor(this.parcela.getValor().divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP));
        }

        if (valorDiferenca.compareTo(BigDecimal.ZERO) > 0) {
            this.parcela.setValor(this.valorTotalProtocolo.subtract(valorDiferenca));
            this.parcela.setValor(this.parcela.getValor().divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP));
        } else {
            this.parcela.setValor(this.valorTotalProtocolo);
        }

        if (this.parcela.getValor() != null && this.parcela.getValor().compareTo(BigDecimal.ZERO) >= 0) {
            this.parcela.setTipo(TipoFinanceiro.ENTRADA);
        } else {
            this.parcela.setTipo(TipoFinanceiro.SAIDA);
        }

        this.parcela.setDataVencimento(new Date());
        this.parcela.setGerador(usuarioServico.getUsuarioLogado());

        if (this.protocolo.isProtocoloAntigo()) {
            this.parcela.setRefProtocoloAntigo(true);
        }

        if (this.parcela.getValor() != null && this.parcela.getValor().compareTo(BigDecimal.ZERO) > 0) {
            this.parcela.setValor(this.parcela.getValor().divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP));
        }

        if (novo && this.parcela.getValor().compareTo(BigDecimal.ZERO) != 0) {
            this.protocolo.getParcelas().add(this.parcela);
        } else if (this.protocolo.getParcelas().contains(this.parcela) && this.parcela.getValor().compareTo(BigDecimal.ZERO) == 0) {
            this.protocolo.getParcelas().remove(this.parcela);
        }

        boolean quitada = true, estorno = false;
        Date dataUltimaParcela = protocolo.getDataQuitacao();

        if (Utils.isEmpty(this.protocolo.getParcelas())) {
            dataUltimaParcela = this.protocolo.getDataProtocolo();
        }

        for (Parcela parcela1 : this.protocolo.getParcelas()) {
            if (parcela1.getValor().equals(BigDecimal.ZERO) && !parcela1.isQuitada()) {
                parcela1.setQuitada(true);
                parcela1.setDataPagamento(new Date());
            }

            if (!parcela1.isQuitada()) {
                quitada = false;
                if (parcela1.getTipo().equals(TipoFinanceiro.SAIDA)) {
                    estorno = true;
                }
                break;
            }
            dataUltimaParcela = parcela1.getDataPagamento();
        }

        this.protocolo.setQuitado(quitada);

        if (quitada || estorno) {
            for (ProtocoloItem item : protocoloItens) {
                if (item.getDataQuitacao() == null) {
                    item.setDataQuitacao(dataUltimaParcela);
                    if (item.getDataConversao() == null && item.getAtoItem().isConversaoImediata()) {
                        if (!(item.getAtoItem().isPadrao() && item.getAtoItem().getTipoSelo() != null && item.isGratuidade())) {
                            item.setSelado(true);
                            item.setDataSelagem(new Date());
                            item.setDataConversao(new Date());
                        }
                    }
                }
            }
        }
    }

    public void salvar() {
        if (this.protocolo.getCliente() != null) {
            if (!Utils.isEmpty(this.protocolo.getCliente().getTelefones())) {
                for (Telefone telefone : this.protocolo.getCliente().getTelefones()) {
                    if ((!Utils.isEmpty(telefone.getDdd()) && telefone.getDdd().length() > 2) || (!Utils.isEmpty(telefone.getNumero()) && Caracteres.removecaracter(telefone.getNumero()).length() > 9)) {
                        Mensagem.messagemError("Telefone do cliente está incorreto. Por favor, verifique se o DDD ou o número está preenchido de forma correta. ");
                        return;
                    }
                }
            }
            if (this.protocolo.getCliente().getTipoPessoa().equals(TipoPessoa.FISICA)) {
                this.protocolo.setIssRetidoNaFonte(false);
            }
        }

        salvar(true);
    }

    public void salvar(boolean msg) {

        if (configuracaoSistema.getEnvioRpsTeste() != null && !configuracaoSistema.getEnvioRpsTeste()) {
            if (protocolo.getCliente() != null) {
                String erro = PessoaUtils.erroCliente(protocolo.getCliente());
                if (!Utils.isEmpty(erro)) {
                    Mensagem.messagemError("Erro ao validar o cliente. " + erro.replace("obrigatórios", "obrigatórios para o envio da Nota Fiscal"));
                    return;
                }
            }
        }

        if (configuracaoSistemaServico.verificarSeESeloDigital("MA") && this.protocolo.getCliente() == null) {
            Mensagem.messagemError("Cliente é obrigatório !");
            return;
        }

        validarAtoComClienteNoProcesso = false;

        for (ProtocoloItem item : protocoloItens) {
            if (item.getAtoItem().isRequerClienteComCpf() && configuracaoSistema.isValidarAtoComClienteNoProcesso()) {
                validarAtoComClienteNoProcesso = true;
            }
        }

        if (validarAtoComClienteNoProcesso && this.protocolo.getCliente() == null) {
            Mensagem.messagemError("Cliente é obrigatório!");
            abreDialogCliente = true;
            return;
        }

        if (controlesalva) {
            return;
        } else {
            controlesalva = true;
        }

        criarParcela();

        if (this.protocolo.getCompetencia() != null && this.protocolo.getCompetencia().isTipoFirma()) {
            this.protocolo.setProtocoloFirmaAutenticacaoReconhecimento(true);
        }
        verificarProtocolo();


        // remover número hard code
        if (protocoloServico.codigoExistente(this.protocolo.getCodigo(), protocolo.getId(), protocolo.getTipoProtocolo())) {

            Mensagem.messagemWarn("Código ja cadastrado");
        } else if (this.protocolo.isProtocoloAntigo()
                && !this.protocolo.getCategoria().equals(CategoriaProtocolo.CERTIDÃO)
                && this.protocolo.getCodigo() > 259999) {

            Mensagem.messagemWarn("Código inválido, verifique a numeração do protocolo.");
        } else {

            if (this.protocoloVinculado != null && this.protocoloVinculado.getId() != null) {
                this.protocolo.setProtocolosVinculados(Arrays.asList(this.protocoloVinculado));
            }

            // salvando protocolo
            if (this.protocolo.getTipoProtocolo().equals(TipoProtocolo.PROTOCOLO)) {

                try {

                    if (!this.protocoloItens.isEmpty()) {

                        for (ProtocoloItem item : this.protocoloItens) {
                            item.setProtocolo(this.protocolo);
                            if (item.getAtoItem().getPrazo() != null && item.getAtoItem().getPrazo() > this.protocolo.getPrazo()) {
                                this.protocolo.setPrazo(item.getAtoItem().getPrazo());
                            }
                        }

                        this.protocolo.setProtocoloItems(this.protocoloItens);

                        if (!Utils.isEmpty(this.protocolo.getAnexos())) {
                            try {

                                anexoServico.setarOrdemInsercao(this.protocolo.getAnexos());

                                anexoServico.gravarAnexos(this.protocolo.getAnexos());
                            } catch (IOException ex) {
                                Mensagem.messagemError("Não foi possível gravar os anexos!");
                            }
                        }

                        if (!Utils.isEmpty(this.anexosRemovidos)) {
                            try {
                                for (Anexo a : this.anexosRemovidos) {
                                    anexoServico.excluirArquivo(new File(a.getUrl() + a.getNome()));
                                    this.anexosRemovidos = new ArrayList<Anexo>();
                                }
                            } catch (FileNotFoundException ex) {
                                Mensagem.messagemError("Não foi possível excluir os anexos!");
                            }
                        }

                        // salvar a porcentagem mesmo que o protocolo tem desconto em dinheiro, isso serve para facilitar os relatorios.
                        if (protocolo.getTipoDesconto() != null && protocolo.getTipoDesconto().equals(TipoDesconto.DINHEIRO)) {
                            if (valorTotalProtocoloSemDesconto != null && !valorTotalProtocoloSemDesconto.equals(BigDecimal.ZERO)) {
                                protocolo.setDescontoPorcentagem(protocolo.getDescontoDinheiro().divide(valorTotalProtocoloSemDesconto, 8, RoundingMode.HALF_UP));
                            }
                        }

                        // salvar protocolo
                        criarParcela();

                        // fixar o cliente como retido na fonte
                        if (this.protocolo.isIssRetidoNaFonte()) {
                            if (this.protocolo.getCliente() != null) {
                                this.protocolo.getCliente().setIssRetidoNaFonte(true);

                                pessoaServico.atualizarPessoa(this.protocolo.getCliente());
                            }
                        }

                        if (this.protocolo.getId() == null) {
                            this.protocolo.setUsuarioCadastro(usuarioServico.getUsuarioLogado());
                            if (!Utils.isEmpty(protocolo.getTramites())) {
                                protocolo.setTramites(new ArrayList<Tramite>());
                            }

                            if (protocolo.getTipoProtocolo().equals(TipoProtocolo.PROTOCOLO)) {

                                Tramite tramite = new Tramite();
                                tramite.setProtocolo(this.protocolo);
                                tramite.setResponsavel(usuarioServico.getUsuarioLogado());

                                if (configuracaoSistema != null && configuracaoSistema.isTramitarProcessosFirmaParaEntrega() && protocolo.getCompetencia() != null && protocolo.getCompetencia().isTipoFirma()) {
                                    tramite.setSituacaoTramite(SituacaoTramite.FINALIZADO);
                                } else {
                                    tramite.setSituacaoTramite(SituacaoTramite.ABERTO);
                                }

                                tramite.setSetor(usuarioServico.getUsuarioLogado().getSetor());
                                tramite.setAtual(true);
                                this.protocolo.getTramites().add(tramite);
                            }

                            protocoloServico.save(this.protocolo);

                            if (msg) {

                                String url = "visualizarProtocolo.xhtml?protocoloId=" + this.protocolo.getId() + "" + (parametroSeloFirmaAutenticacao == null ? "" : parametroSeloFirmaAutenticacao);

                                if (processoFirmaAutenticacao != null) {
                                    //TODO Desabilitado por conta da alteração de firma 
                                    //&& configuracaoSistema.isHabilitarSelagemPreviaFirmaAutenticacao()}) {
                                    url += "&zerarfirma=true";
                                }
                                Mensagem.messagemInfoRedirect("Processo cadastrado com sucesso!", url);

                            }

                            // atualizar protocolo
                        } else {

                            if (this.protocolo.getStatusProtocolo().equals(StatusProtocolo.PRATICADO)) {
                                this.protocolo.setIssAlteradoParaEnvioDoRps(true);

                                if (configuracaoSistema.isPermitirAdicionarServicos()) {
                                    protocolo.setStatusProtocolo(ProtocoloUtils.statusProtocoloAtual(protocolo));
                                }
                            }

                            for (ProtocoloItem item : protocolosItensRemovidos) {
                                if (item.getClasse() != null) {

                                    if (item.getClasse().equals(Classe.ARQUIVAMENTO_FIRMA)) {
                                        ReconhecimentoFirma firma = reconhecimentoFirmaServico.pesquisar(item.getIdVinculado());

                                        if (firma != null) {
                                            firma.setProtocoloItems(null);
                                            reconhecimentoFirmaServico.update(firma);
                                        }
                                    } else if (item.getClasse().equals(Classe.AUTENTICACAO)) {
                                        Autenticacao firma = autenticacaoServico.pesquisar(item.getIdVinculado());

                                        if (firma != null) {
                                            firma.setProtocoloItems(null);
                                            autenticacaoServico.update(firma);
                                        }

                                    } else if (item.getClasse().equals(Classe.RECONHECIMENTO_FIRMA)) {
                                        ReconhecimentoFirma firma = reconhecimentoFirmaServico.pesquisar(item.getIdVinculado());

                                        if (firma != null) {
                                            firma.setProtocoloItems(null);
                                            reconhecimentoFirmaServico.update(firma);
                                        }

                                    } else if (item.getClasse().equals(Classe.DUT)) {
                                        DutEletronico firma = dutEletronicoServico.pesquisar(item.getIdVinculado());

                                        if (firma != null) {
                                            firma.setProtocoloItems(null);
                                            dutEletronicoServico.update(firma);
                                        }
                                    }
                                }
                                item.setClasse(null);
                                item.setIdVinculado(null);
                            }

                            protocoloServico.update(this.protocolo);
                            if (msg) {
                                Mensagem.messagemInfoRedirect("Processo atualizado com sucesso!", "visualizarProtocolo.xhtml?protocoloId=" + this.protocolo.getId() + "" + (parametroSeloFirmaAutenticacao == null ? "" : parametroSeloFirmaAutenticacao));
                            }
                        }

                        this.protocolo = protocoloServico.pesquisar(protocolo.getId());

                        if (!Utils.isEmpty(protocolo.getParcelas())) {
                            for (Parcela p : protocolo.getParcelas()) {
                                p.setProtocolo(this.protocolo);

                                //TODO COLOCAR GUIA EM PERNAMBUCO
                                String descricao = "Processo: " + p.getProtocolo().getCodigo();

                                if (notarioPernambuco() && !Utils.isEmpty(p.getProtocolo().getGuia())) {
                                    descricao += " Guia: " + p.getProtocolo().getGuia();
                                }

                                descricao += ". Cliente: " + (p.getProtocolo().getCliente() != null
                                        ? PessoaUtils.formatarPessoa(p.getProtocolo().getCliente()) : " Cliente final.");

                                p.setDescricao(descricao);

                                parcelaServico.update(p);
                            }
                            // se o processo não tiver parcelas converte os items que sejam de conversão imediata
                        } else {
                            int quantidadePraticados = 0;
                            for (ProtocoloItem item : this.protocolo.getProtocoloItems()) {
                                if (item.getAtoItem().isConversaoImediata()) {
                                    Ato ato = item.getAtoItem();
                                    if (!(ato.isPadrao() && ato.getTipoSelo() != null && item.isGratuidade())) {
                                        item.setSelado(true);
                                        item.setDataSelagem(new Date());
                                        item.setDataConversao(new Date());
                                        item.setDataQuitacao(new Date());
                                    }
                                }

                                if (item.isSelado() && item.getDataConversao() != null) {
                                    quantidadePraticados++;
                                }
                            }
                            // status do processo
                            if (this.protocolo.getProtocoloItems().size() == quantidadePraticados) {
                                this.protocolo.setStatusProtocolo(StatusProtocolo.PRATICADO);
                            } else if (quantidadePraticados == 0) {
                                this.protocolo.setStatusProtocolo(StatusProtocolo.ABERTO);
                            } else {
                                this.protocolo.setStatusProtocolo(StatusProtocolo.PARCIALMENTE_PRATICADO);
                            }



                            protocoloServico.update(protocolo);
                        }

                    } else {
                        Mensagem.messagemWarn("Adicione pelo menos um serviço!");
                    }

                } catch (Exception e) {
                    System.err.println(e);
                    if (e instanceof EJBException) {
                        if (!Utils.isEmpty(e.getMessage())) {
                            MessageUtils.warn(e.getMessage());
                        } else {
                            MessageUtils.warn("Erro ao atualizar o Processo. O item removido possui um ato vinculado!");
                        }
                    } else {
                        MessageUtils.warn("Ocorreu um erro desconhecido!");
                    }

                    System.err.println(e);
                }

                // salvando orçamento
            } else {
                // fixar o cliente como retido na fonte
                if (this.protocolo.isIssRetidoNaFonte()) {
                    if (this.protocolo.getCliente() != null) {
                        this.protocolo.getCliente().setIssRetidoNaFonte(true);

                        pessoaServico.atualizarPessoa(this.protocolo.getCliente());
                    }
                }

                if (!this.protocoloItens.isEmpty()) {

                    for (ProtocoloItem item : this.protocoloItens) {
                        item.setProtocolo(this.protocolo);

                        if (item.getAtoItem().getPrazo() != null && item.getAtoItem().getPrazo() > this.protocolo.getPrazo()) {
                            this.protocolo.setPrazo(item.getAtoItem().getPrazo());
                        }
                    }

                    this.protocolo.setProtocoloItems(this.protocoloItens);

                    // salvar orçamento
                    if (this.protocolo.getId() == null) {
                        this.protocolo.setUsuarioCadastro(usuarioServico.getUsuarioLogado());
                        protocoloServico.save(this.protocolo);
                        Mensagem.messagemInfoRedirect("Orçamento cadastrado com sucesso!", "visualizarProtocolo.xhtml?protocoloId=" + this.protocolo.getId());

                        // atualizar orçamento
                    } else {
                        protocoloServico.update(this.protocolo);
                        Mensagem.messagemInfoRedirect("Orçamento atualizado com sucesso!", "visualizarProtocolo.xhtml?protocoloId=" + this.protocolo.getId());
                    }

                } else {
                    Mensagem.messagemWarn("Adicione pelo menos um serviço!");
                }

            }

        }
    }

    public String obterInteressado() {
        Pessoa pessoa = this.protocolo.getCliente();

        if (pessoa != null && pessoa.getTipoPessoa() != null) {
            if (pessoa.getTipoPessoa() == TipoPessoa.FISICA) {
                return pessoa.getNome() + ", CPF " + adicionarMascaraCPF(pessoa.getCpf());
            } else {
                return pessoa.getRazaoSocial() + ", CNPJ " + adicionarMascaraCNPJ(pessoa.getCnpj());
            }
        } else {
            return "";
        }

    }

    public void selecionarTipoAnexo(FileUploadEvent event) {
        try {
            this.arquivo = event.getFile();
            this.protocolo.getAnexos().add(anexoServico.adicionarArquivo(this.arquivo));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void adicionarAnexo() {

        try {

            this.protocolo.getAnexos().add(anexoServico.adicionarArquivo(this.arquivo));

        } catch (FileNotFoundException e) {
            System.err.println(e);
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            System.err.println(e);
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            System.err.println(e);
            Logger.getLogger(ManagerCriarProtocolo.class.getName()).log(Level.SEVERE, null, e);
        }

        this.anexo = new Anexo();

    }

    public void removerAnexo(Anexo anexo) {
        this.protocolo.getAnexos().remove(anexo);
        this.anexosRemovidos.add(anexo);
    }

    public void removerTodosAnexos() {
        this.anexosRemovidos.addAll(this.protocolo.getAnexos());
        this.protocolo.getAnexos().clear();
    }

    public String adicionarMascaraCNPJ(String string) {
        return Caracteres.adicionarMascara(string, Caracteres.cnpjMask);
    }

    public String adicionarMascaraCPF(String string) {
        return Caracteres.adicionarMascara(string, Caracteres.cpfMask);
    }

    public void atualizarPrograma() {
        if (this.protocolo.getTipoProgramaProcesso() != null && this.protocolo.getTipoProgramaProcesso().getTipoDesconto() != null) {
            this.protocolo.setTipoDesconto(this.protocolo.getTipoProgramaProcesso().getTipoDesconto());
            if (this.protocolo.getTipoDesconto() != null) {
                if (this.protocolo.isProtocoloAntigo()) {
                    if (this.protocolo.getDescontoPorcentagem() == null || !this.protocolo.getDescontoPorcentagem().setScale(0).equals(BigDecimal.ONE)) {
                        if (this.protocolo.getTipoDesconto().equals(TipoDesconto.PORCENTAGEM)) {
                            this.protocolo.setDescontoPorcentagem(this.protocolo.getTipoProgramaProcesso().getDescontoPorcentagem());
                        } else if (this.protocolo.getTipoDesconto().equals(TipoDesconto.DINHEIRO)) {
                            this.protocolo.setDescontoPorcentagem(this.protocolo.getTipoProgramaProcesso().getDescontoPorcentagem());
                        }
                    }
                } else {
                    if (this.protocolo.getTipoDesconto().equals(TipoDesconto.PORCENTAGEM)) {
                        this.protocolo.setDescontoPorcentagem(this.protocolo.getTipoProgramaProcesso().getDescontoPorcentagem());
                    } else if (this.protocolo.getTipoDesconto().equals(TipoDesconto.DINHEIRO)) {
                        this.protocolo.setDescontoPorcentagem(this.protocolo.getTipoProgramaProcesso().getDescontoPorcentagem());
                    }
                }
            }
        } else {
            this.protocolo.setDescontoPorcentagem(BigDecimal.ZERO);
            this.protocolo.setTipoDesconto(null);
        }

        calcularDesconto();
    }

    public void adicionarParte() {
        Pessoa pessoa = this.parte.getPessoa();
        pessoa.setCategoriaPessoa(CategoriaPessoa.CLIENTE);

        if (this.tipoPessoaParte == TipoPessoa.FISICA && !Utils.isEmpty(pessoa.getCpf())) {
            if (pessoaServico.isCPFUnico(pessoa.getCpf(), pessoa.getId())) {
                pessoa.setCpf(Caracteres.removecaracter(pessoa.getCpf()));
                pessoa.setSlugNome(Caracteres.removeCaracteresEspeciais(pessoa.getNome()));
                pessoa.setTipoPessoa(tipoPessoaParte);
                if (!this.protocolo.getPartes().contains(parte)) {
                    this.protocolo.getPartes().add(this.parte);
                }
                limparParte();
            } else {
                Mensagem.messagemWarn("CPF já cadastrado!");
            }
        } else if (this.tipoPessoaParte == TipoPessoa.JURIDICA && !Utils.isEmpty(pessoa.getCnpj())) {
            if (pessoaServico.isCNPJUnico(pessoa.getCnpj(), pessoa.getId())) {
                pessoa.setCnpj(Caracteres.removecaracter(pessoa.getCnpj()));
                pessoa.setSlugNome(Caracteres.removeCaracteresEspeciais(pessoa.getRazaoSocial()));
                pessoa.setTipoPessoa(tipoPessoaParte);
                if (!this.protocolo.getPartes().contains(parte)) {
                    this.protocolo.getPartes().add(this.parte);
                }
                limparParte();
            } else {
                Mensagem.messagemWarn("CNPJ já cadastrado!");
            }
        } else {
            pessoa.setSlugNome(Caracteres.removeCaracteresEspeciais(pessoa.getNome()));
            pessoa.setTipoPessoa(tipoPessoaParte);
            this.protocolo.getPartes().add(this.parte);
            limparParte();
        }
    }

    public void limparParte() {
        this.parte = new Parte();
        Pessoa pessoa = new Pessoa();
        tipoPessoaParte = TipoPessoa.FISICA;
        this.parte.setPessoa(pessoa);
    }

    public void editarParte(Parte parte) {
        this.parte = parte;
        tipoPessoaParte = parte.getPessoa().getTipoPessoa();
    }

    public void removerParte(Parte parte) {
        this.protocolo.getPartes().remove(parte);
    }

    public List<Qualidade> autocompletarQualidade(String query) {
        return qualidadeServico.autocompletar(query, new AtoTipoEP());
    }

    /**
     * Calcula o valor dos impostos do protocolo item passado por parâmetro.
     * Caso seja enviado o valor null então os protocolos do campo
     * this.protocoloItens terão os impostos calculados.
     *
     * @param protocoloItem
     */
    private void calcularImpostosProtocoloItens(ProtocoloItem protocoloItem) {
        if (!Utils.isEmpty(this.impostos)) {

            List<ProtocoloItem> items = protocoloItem == null ? protocoloItens : Arrays.asList(protocoloItem);

            for (ProtocoloItem item : items) {
                calcularImpostoProtocoloItem(item);
            }
        }
    }

    /**
     * Calcula o valor dos impostos do protocolo item passado por parâmetro.
     *
     * O calculo só serão feitos caso não tiver sido calculado os impostos do
     * protocolo item ou, se já tiver sido calculado, serão recalculados se o
     * desconto do protocolo item for maior que 0(zero)
     *
     * @param item
     */
    private void calcularImpostoProtocoloItem(ProtocoloItem item) {
        item.setProtocolo(protocolo);
        ProtocoloItemHelper.calcularImpostos(item, this.impostos);
    }

    public BigDecimal valorImpostosComDesconto() {
        BigDecimal valor = BigDecimal.ZERO;

        for (ImpostoProtocolo impostoProtocolo : protocolo.getImpostos()) {
            impostoProtocolo.setValorTotal(BigDecimal.ZERO);
            impostoProtocolo.setValorTotalComDesconto(BigDecimal.ZERO);
        }
        protocolo.gerarImpostosMap();

        if (!Utils.isEmpty(this.protocoloItens)) {
            for (ProtocoloItem item : protocoloItens) {
                item.setImpostosUnitario(null);
                if (!Utils.isEmpty(item.getImpostos())) {
                    for (ImpostoProtocoloItem impostoProtocoloItem : item.getImpostos()) {
                        // totalizar impostos para casos futuros
                        ImpostoProtocolo impostoProtocolo = protocolo.getImpostosMap().get(impostoProtocoloItem.getImposto().getId());
                        if (!item.isGratuidade()) {
                            valor = valor.add(impostoProtocoloItem.getValorTotalComDesconto());
                            if (impostoProtocolo == null) {
                                impostoProtocolo = new ImpostoProtocolo();
                                impostoProtocolo.setImposto(impostoProtocoloItem.getImposto());
                                impostoProtocolo.setValorTotal(BigDecimal.ZERO);
                                impostoProtocolo.setValorTotalComDesconto(BigDecimal.ZERO);
                                protocolo.getImpostos().add(impostoProtocolo);
                            }
                        }
                        impostoProtocolo.setValorTotal(impostoProtocolo.getValorTotal().add(impostoProtocoloItem.getValorTotal()));
                        impostoProtocolo.setValorTotalComDesconto(impostoProtocolo.getValorTotalComDesconto().add(impostoProtocoloItem.getValorTotalComDesconto()));
                    }
                }
            }
        }

        return valor;
    }

    public BigDecimal valorImpostos() {
        BigDecimal valor = BigDecimal.ZERO;

        if (!Utils.isEmpty(this.protocoloItens)) {
            for (ProtocoloItem item : protocoloItens) {
                if (!Utils.isEmpty(item.getImpostos())) {
                    for (ImpostoProtocoloItem impostoProtocoloItem : item.getImpostos()) {
                        if (!item.isGratuidade()) {
                            valor = valor.add(impostoProtocoloItem.getValorTotal());
                        }
                    }
                }
            }
        }
        return valor;
    }

    public BigDecimal valorImpostoProtocoloItem(Imposto imposto) {
        protocolo.gerarImpostosMap();
        ImpostoProtocolo impostoProtocolo = protocolo.getImpostosMap().get(imposto.getId());
        return impostoProtocolo != null ? impostoProtocolo.getValorTotalComDesconto() : BigDecimal.ZERO;
    }

    public BigDecimal calcularEmolumentoItem(ProtocoloItem pi) {

        if (!Utils.isEmpty(pi.getProtocoloCampos())) {

            for (ProtocoloCampo protocoloCampo : pi.getProtocoloCampos()) {

                // Campo usado como filtro na tabela de emolumentos
                if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.FILTRO) {
                    try {
                        return emolumentoItemServico.pesquisar(pi.getAtoItem(), new BigDecimal(protocoloCampo.getValor())).getValorCartorio();

                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato com o valor digitado.");
                    }

                    // Campo usado como valor do ato
                } else if (protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.VALOR || protocoloCampo.getAtoCampo().getAplicacao() == AplicacaoCampo.VALOR_CARTORIO) {

                    try {

                        return (new BigDecimal(protocoloCampo.getValor()));

                    } catch (Exception e) {
                        System.err.println(e);
                        Mensagem.messagemWarn("Não existe nenhum item na tabela de Emolumentos para esse Ato.");
                    }

                }

            }
        }

        //PARA COMPENSAR EDICAO DE ATOS APOS MUDANCA DE TABELA
        if (emolumentoItemServico.pesquisar(pi.getAtoItem()) == null) {
            return pi.getValorCartorio();
        }

        return emolumentoItemServico.pesquisar(pi.getAtoItem()).getValorCartorio();
    }

    public void registrarNoLivro() {
        try {

            for (ProtocoloItem item : this.protocoloItens) {
                if (item.getAtoItem().isConverterAoProtocolar() && item.getDataConversao() == null) {

                    item.setSelado(true);
                    item.setDataSelagem(new Date());
                    item.setDataConversao(new Date());
                    item.setUsuarioSelagem(usuarioServico.getUsuarioLogado());
                }
            }

            if (this.protocolo.getDataRegistroLivro() == null) {
                this.protocolo.setDataRegistroLivro(new Date());
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void managerCriarProtocolo() {
    }

    public void cancelaModelor() {
        this.modeloProcesso = null;
    }

    public void carregarModelo() {
        for (ItemModeloProcesso itemModeloProcesso : modeloProcesso.getItemModeloProcessos()) {
            if (!Utils.isEmpty(itemModeloProcesso.getAto().getAtoCampos())) {
                for (AtoCampo atoCampo : itemModeloProcesso.getAto().getAtoCampos()) {
                    ProtocoloCampo protocoloCampo = new ProtocoloCampo();
                    protocoloCampo.setAtoCampo(atoCampo);
                    itemModeloProcesso.getCampos().add(protocoloCampo);
                }
            }
        }
    }

    public void selecionarModelo() {

        this.protocolo = modeloProcessoServico.criarProcesso(modeloProcesso);
        this.protocoloItens = this.protocolo.getProtocoloItems();
        this.gratuidadeItem = this.protocolo.isGratuidade();
        this.parcelas = this.protocolo.getParcelas();

        if (this.protocolo.getValorCartorioComDesconto() != null && this.protocolo.getValorFundo() != null) {
            this.valorTotalProtocolo = this.protocolo.getValorCartorioComDesconto();//.add(this.protocolo.getValorFundo()).add(this.protocolo.getValorTotalSelo());
            this.valorTotalProtocoloSemDesconto = this.protocolo.getValorCartorio().add(this.protocolo.getValorFundo()).add(this.protocolo.getValorTotalSelo()).add(valorImpostos());

        } else {
            this.protocolo.setValorCartorioComDesconto(BigDecimal.ZERO);
            this.protocolo.setValorFundo(BigDecimal.ZERO);
            this.valorTotalProtocolo = BigDecimal.ZERO;
            this.valorTotalProtocoloSemDesconto = BigDecimal.ZERO;
        }

        RequestContext.getCurrentInstance().execute("modeloProcessoDialog.hide();");
    }

    public void limitarQuantidadePrenotacao(Ato ato) {
        this.limitarQuantidadePrenotacao = false;

        if ((ato.isPrenotacao() && !configuracaoSistema.isPermitirMaisDeUmaPrenotacaoPorProcesso())
                || (ato.isElaboracaoPeticao() && !configuracaoSistema.isPermitirMaisDeUmaElaboracaoDePeticaoPorProcesso())) {
            this.quantidade = 1L;
            this.limitarQuantidadePrenotacao = true;
        }
    }

    public List<SelectItem> autocompletarBaseLegalAto(Ato ato) {
        List<BaseLegal> bases = baseLegalServico.autocomplete("", ato);
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem(null, "Nenhuma"));
        for (BaseLegal baseLegal : bases) {
            itens.add(new SelectItem(baseLegal, baseLegal.getDescricaoResumida()));
        }
        return itens;
    }

    public List<BaseLegal> autocompletarBaseLegal(String descricao) {
        List<BaseLegal> basesLegais = baseLegalServico.autocomplete(descricao, ato);
        BaseLegal baseLegalPadrao = new BaseLegal();
        baseLegalPadrao.setSlugDescricao("Nenhuma");
        baseLegalPadrao.setDescricao("Nenhuma");
        baseLegalPadrao.setDescricaoResumida("Nenhuma");
        if (!Utils.isEmpty(basesLegais)) {
            basesLegais.add(0, baseLegalPadrao);
        } else {
            basesLegais.add(baseLegalPadrao);
        }

        return basesLegais;
    }

    public void criarProcessoRetificador() {
        this.protocolo = protocoloServico.criarProcessoRetificador(this.protocolo.getProtocoloItems());
        this.protocoloItens = this.protocolo.getProtocoloItems();
    }

    public void validarQuantidadeServico() {
        if (this.quantidade == 9999999999L) {
            Mensagem.messagemError("O campo Quantidade excede o tamanho permitido! Por favor informe valores entre 1 e X");
        }
    }

    public void selecionarAnexoParcela(FileUploadEvent event) {
        this.arquivo = event.getFile();
    }

    public void adicionarAnexoParcela(Parcela parcela) {
        try {
            if (Utils.isEmpty(parcela.getAnexos())) {
                parcela.setAnexos(new ArrayList<Anexo>());
            }
            parcela.getAnexos().add(anexoServico.adicionarArquivo(this.arquivo));

            if (!Utils.isEmpty(parcela.getAnexos())) {
                try {
                    anexoServico.gravarAnexos(parcela.getAnexos());
                    parcelaServico.atualizar(parcela);

                    Mensagem.messagemInfo(Mensagem.SuccessFull);
                } catch (IOException ex) {
                    Mensagem.messagemError("Não foi possível gravar os anexos!");
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManagerPesquisarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManagerPesquisarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManagerPesquisarProtocolo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean permitirEditarIssRetidoNaFonte() {
        return !(verifyPermissionActionRender("EDITAR ISS RETIDO") && !protocolo.isIssAlteradoParaEnvioDoRps());
    }

    public void atribuirTipoGratuidadeProtocoloItem() {
        if (this.protocolo.isGratuidade()) {
            this.tipoGratuidadeProtocoloItem = this.protocolo.getTipoGratuidade();
        } else {
            this.tipoGratuidadeProtocoloItem = null;
            this.protocolo.setTipoGratuidade(null);
        }
    }

    public Ato getAto() {
        return ato;
    }

    public void setAto(Ato ato) {
        this.ato = ato;
    }

    public Protocolo getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

    public List<ProtocoloItem> getProtocoloItens() {
        return protocoloItens;
    }

    public void setProtocoloItens(List<ProtocoloItem> protocoloItens) {
        this.protocoloItens = protocoloItens;
    }

    public EmolumentoItem getEmolumentoItem() {
        return emolumentoItem;
    }

    public void setEmolumentoItem(EmolumentoItem emolumentoItem) {
        this.emolumentoItem = emolumentoItem;
    }

    public BigDecimal getValorTotalProtocolo() {
        return valorTotalProtocolo;
    }

    public void setValorTotalProtocolo(BigDecimal valorTotalProtocolo) {
        this.valorTotalProtocolo = valorTotalProtocolo;
    }

    public List<AtoCampo> getAtoCampos() {
        return atoCampos;
    }

    public void setAtoCampos(List<AtoCampo> atoCampos) {
        this.atoCampos = atoCampos;
    }

    public List<ProtocoloCampo> getProtocoloCampos() {
        return protocoloCampos;
    }

    public void setProtocoloCampos(List<ProtocoloCampo> protocoloCampos) {
        this.protocoloCampos = protocoloCampos;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public boolean isGratuidadeItem() {
        return gratuidadeItem;
    }

    public void setGratuidadeItem(boolean gratuidadeItem) {
        this.gratuidadeItem = gratuidadeItem;
    }

    public String getTextoNotaDevolucao() {
        return textoNotaDevolucao;
    }

    public void setTextoNotaDevolucao(String textoNotaDevolucao) {
        this.textoNotaDevolucao = textoNotaDevolucao;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public Cartorio getCartorio() {
        return cartorio;
    }

    public void setCartorio(Cartorio cartorio) {
        this.cartorio = cartorio;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public Anexo getAnexo() {
        return anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }

    public BigDecimal getValorCobradoProtocoloItemAnterior() {
        return valorCobradoProtocoloItemAnterior;
    }

    public void setValorCobradoProtocoloItemAnterior(BigDecimal valorCobradoProtocoloItemAnterior) {
        this.valorCobradoProtocoloItemAnterior = valorCobradoProtocoloItemAnterior;
    }

    public boolean isCobrancaIndevidaProtocoloItem() {
        return cobrancaIndevidaProtocoloItem;
    }

    public void setCobrancaIndevidaProtocoloItem(boolean cobrancaIndevidaProtocoloItem) {
        this.cobrancaIndevidaProtocoloItem = cobrancaIndevidaProtocoloItem;
    }

    public SelectBooleanButton getGratuitoBooleanButton() {
        return gratuitoBooleanButton;
    }

    public void setGratuitoBooleanButton(SelectBooleanButton gratuitoBooleanButton) {
        this.gratuitoBooleanButton = gratuitoBooleanButton;
    }

    public BigDecimal getValorTotalProtocoloSemDesconto() {
        return valorTotalProtocoloSemDesconto;
    }

    public void setValorTotalProtocoloSemDesconto(BigDecimal valorTotalProtocoloSemDesconto) {
        this.valorTotalProtocoloSemDesconto = valorTotalProtocoloSemDesconto;
    }

    @Override
    public UserSystem getUserSystem() {
        return usuarioServico.getUsuarioLogado();
    }

    public void removerCliente() {
        this.protocolo.setCliente(null);
        this.protocolo.setIssRetidoNaFonte(false);
    }

    public void removerRequerente() {
        this.protocolo.setRequerente(null);
    }

    public boolean isProtocoloFirma() {
        return protocoloFirma;
    }

    public void setProtocoloFirma(boolean protocoloFirma) {
        this.protocoloFirma = protocoloFirma;
    }

    public Protocolo getProtocoloVinculado() {
        return protocoloVinculado;
    }

    public void setProtocoloVinculado(Protocolo protocoloVinculado) {
        this.protocoloVinculado = protocoloVinculado;
    }

    public boolean isEditarApenasAnexo() {
        return editarApenasAnexo;
    }

    public void setEditarApenasAnexo(boolean editarApenasAnexo) {
        this.editarApenasAnexo = editarApenasAnexo;
    }

    public Parte getParte() {
        return parte;
    }

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public TipoPessoa getTipoPessoaParte() {
        return tipoPessoaParte;
    }

    public void setTipoPessoaParte(TipoPessoa tipoPessoaParte) {
        this.tipoPessoaParte = tipoPessoaParte;
    }

    public boolean clienteObrigatorio() {
        return configuracaoSistema.isValidarAtoComClienteNoProcesso() && this.validarAtoComClienteNoProcesso;
    }

    public boolean isAdministradorEdicaoProcesso() {
        return administradorEdicaoProcesso;
    }

    public void setAdministradorEdicaoProcesso(boolean administradorEdicaoProcesso) {
        this.administradorEdicaoProcesso = administradorEdicaoProcesso;
    }

    public boolean isLimitarEdicaoProcesso() {
        return limitarEdicaoProcesso;
    }

    public void setLimitarEdicaoProcesso(boolean limitarEdicaoProcesso) {
        this.limitarEdicaoProcesso = limitarEdicaoProcesso;
    }

    public void setImpostos(List<Imposto> impostos) {
        this.impostos = impostos;
    }

    public List<Imposto> getImpostos() {
        return this.impostos;
    }

    public ModeloProcesso getModeloProcesso() {
        return modeloProcesso;
    }

    public void setModeloProcesso(ModeloProcesso modeloProcesso) {
        this.modeloProcesso = modeloProcesso;
    }

    public ConfiguracaoSistema getConfiguracaoSistema() {
        return configuracaoSistema;
    }

    public void setConfiguracaoSistema(ConfiguracaoSistema configuracaoSistema) {
        this.configuracaoSistema = configuracaoSistema;
    }

    public boolean isLimitarQuantidadePrenotacao() {
        return limitarQuantidadePrenotacao;
    }

    public void setLimitarQuantidadePrenotacao(boolean limitarQuantidadePrenotacao) {
        this.limitarQuantidadePrenotacao = limitarQuantidadePrenotacao;
    }

    public boolean isAbreDialogCliente() {
        return abreDialogCliente;
    }

    public void setAbreDialogCliente(boolean abreDialogCliente) {
        this.abreDialogCliente = abreDialogCliente;
    }

    public void verificaDialogCliente() {
        if (abreDialogCliente) {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("dialogPesquisarCliente.show();");
        }
    }

    public boolean notarioPernambuco() {
        if (this.cartorio == null) {
            this.cartorio = cartorioServico.obterCartorio();
        }
        String ufSistema = cartorio.getEndereco().getCidade().getEstado().getSigla();
        return (ufSistema != null && ufSistema.equalsIgnoreCase("PE"));
    }

    public boolean isExibirCampoRegistradonoProcesso() {
        return configuracaoSistema.isExibirCampoRegistradoNoProcesso();
    }

    private void mostrarAvisoConsultarSubstitutoTributario() {
        if (configuracaoSistema.isMostrarAvisoProcessoConsultarSubstitutoTributario() && this.protocolo.getCliente().getTipoPessoa().equals(TipoPessoa.JURIDICA)) {
            RequestContext.getCurrentInstance().execute("mostrarAvisoConsultaSubstitutoDialog.show()");
        }
    }

    public boolean notarioCeara() {
        String ufSistema = cartorio.getEndereco().getCidade().getEstado().getSigla();
        return (ufSistema != null && ufSistema.equalsIgnoreCase("CE"));
    }

    public boolean notarioMaranhao() {
        String ufSistema = cartorio.getEndereco().getCidade().getEstado().getSigla();
        return (ufSistema != null && ufSistema.equalsIgnoreCase("MA"));
    }

    public boolean notarioPiaui() {
        String ufSistema = cartorio.getEndereco().getCidade().getEstado().getSigla();
        return (ufSistema != null && ufSistema.equalsIgnoreCase("PI"));
    }

    public void desvincularProtocoloItem() {

        if (this.protocoloItemDesvincular.getClasse() != null) {

            try {
                switch (this.protocoloItemDesvincular.getClasse()) {
                    case DUT:
                        DutEletronico dut = dutEletronicoServico.pesquisar(this.protocoloItemDesvincular.getIdVinculado());
                        dut.getProtocoloItems().remove(this.protocoloItemDesvincular);

                        dutEletronicoServico.atualizar(dut);

                        break;
                    case AUTENTICACAO:
                        Autenticacao autenticacao = autenticacaoServico.pesquisar(this.protocoloItemDesvincular.getIdVinculado());
                        autenticacao.getProtocoloItems().remove(this.protocoloItemDesvincular);

                        autenticacaoServico.update(autenticacao);

                        break;
                    case RECONHECIMENTO_FIRMA:
                        ReconhecimentoFirma reconhecimento = reconhecimentoFirmaServico.pesquisar(this.protocoloItemDesvincular.getIdVinculado());
                        reconhecimento.getProtocoloItems().remove(this.protocoloItemDesvincular);

                        reconhecimentoFirmaServico.atualizar(reconhecimento);

                        break;
                    case ARQUIVAMENTO_FIRMA:
                        break;
                    default:
                        return;
                }

                this.protocoloItemDesvincular.setVinculado(false);
                this.protocoloItemDesvincular.setIdVinculado(null);

                protocoloServico.update(this.protocolo);

                Mensagem.messagemInfoRedirect("Item desvinculado com sucesso !", "criarProtocolo.xhtml?protocoloId=" + this.protocolo.getId());

            } catch (Exception e) {
                System.err.println(e);
                Mensagem.messagemError("Ocorreu um erro ao desvicular o Item. Veja o log para mais detalhes !");
            }
        }

        this.protocoloItemDesvincular = null;
    }

    public boolean permitirDesvincularProtocoloItem(ProtocoloItem item) {

        if (item.isVinculado()) {

            if (item.getClasse() != null) {
                switch (item.getClasse()) {
                    case DUT:
                    case AUTENTICACAO:
                    case RECONHECIMENTO_FIRMA:
                    case ARQUIVAMENTO_FIRMA:
                        return false;
                }

                return true;
            }

            return false;
        }

        return true;
    }

    public TipoGratuidade getTipoGratuidadeProtocoloItem() {
        return tipoGratuidadeProtocoloItem;
    }

    public void setTipoGratuidadeProtocoloItem(TipoGratuidade tipoGratuidadeProtocoloItem) {
        this.tipoGratuidadeProtocoloItem = tipoGratuidadeProtocoloItem;
    }

    public Emolumento getTabelaEmolumento() {
        return tabelaEmolumento;
    }

    public void setTabelaEmolumento(Emolumento tabelaEmolumento) {
        this.tabelaEmolumento = tabelaEmolumento;
    }

    public String getMotivoIsencao() {
        return motivoIsencao;
    }

    public void setMotivoIsencao(String motivoIsencao) {
        this.motivoIsencao = motivoIsencao;
    }

    public ProtocoloItem getProtocoloItemDesvincular() {
        return protocoloItemDesvincular;
    }

    public void setProtocoloItemDesvincular(ProtocoloItem protocoloItemDesvincular) {
        this.protocoloItemDesvincular = protocoloItemDesvincular;
    }

    public boolean isPermitirEdicaoObservacao() {
        return permitirEdicaoObservacao;
    }

    public void setPermitirEdicaoObservacao(boolean permitirEdicaoObservacao) {
        this.permitirEdicaoObservacao = permitirEdicaoObservacao;
    }

    public boolean isPermitirAdicionarServicos() {
        return permitirAdicionarServicos;
    }

    public void setPermitirAdicionarServicos(boolean permitirAdicionarServicos) {
        this.permitirAdicionarServicos = permitirAdicionarServicos;
    }

    public List<Ato> atosFirma(ReconhecimentoFirma reconhecimentoFirma) {
        List<Ato> atosFirma;
        if ((notarioCeara() || notarioMaranhao()) && Utils.isNotEmpty(reconhecimentoFirma.getItemReconhecimentoFirmas()) && reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).isDut()) {
            atosFirma = emolumentoItemServico.obterAtosDut();
        } else if (notarioMaranhao() && Utils.isNotEmpty(reconhecimentoFirma.getItemReconhecimentoFirmas()) && reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).isFinanceiro()) {
            atosFirma = emolumentoItemServico.obterAtoReconhecimentoFimaFinanceiro();
        } else if (notarioPernambuco() && Utils.isNotEmpty(reconhecimentoFirma.getItemReconhecimentoFirmas()) && reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).getFirma() != null
                && reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).getFirma().getPessoa() != null && reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).getFirma().getPessoa().isTabeliaoPreposto()) {
            atosFirma = emolumentoItemServico.obterAtosReconhecimentoFirma(reconhecimentoFirma.getTipoReconhecimento(), reconhecimentoFirma.getItemReconhecimentoFirmas().get(0).getFirma().getPessoa());
        } else {
            atosFirma = emolumentoItemServico.obterAtosReconhecimentoFirma(reconhecimentoFirma.getTipoReconhecimento());
        }
        return atosFirma;
    }

    public List<Ato> atosFirma(ItemReconhecimentoFirma itemReconhecimentoFirma) {
        List<Ato> atosFirma;


        if ((notarioCeara() || notarioMaranhao()) && Utils.isNotEmpty(itemReconhecimentoFirma) && itemReconhecimentoFirma.isDut()) {
            atosFirma = emolumentoItemServico.obterAtosDut();
        } else if (notarioMaranhao() && Utils.isNotEmpty(itemReconhecimentoFirma) && itemReconhecimentoFirma.isFinanceiro()) {
            atosFirma = emolumentoItemServico.obterAtoReconhecimentoFimaFinanceiro();
        } else if (notarioPernambuco() && Utils.isNotEmpty(itemReconhecimentoFirma.getReconhecimentoFirma()) && itemReconhecimentoFirma.getFirma() != null && itemReconhecimentoFirma.getFirma().getPessoa() != null && itemReconhecimentoFirma.getFirma().getPessoa().isTabeliaoPreposto() == true) {
            atosFirma = emolumentoItemServico.obterAtosReconhecimentoFirma(itemReconhecimentoFirma.getReconhecimentoFirma().getTipoReconhecimento(), itemReconhecimentoFirma.getFirma().getPessoa());
        } else {
            atosFirma = emolumentoItemServico.obterAtosReconhecimentoFirma(itemReconhecimentoFirma.getReconhecimentoFirma().getTipoReconhecimento());
        }

        return atosFirma;
    }
}
